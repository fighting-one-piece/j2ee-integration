package org.platform.modules.abstr.storage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.platform.modules.abstr.storage.annotation.IgnoreCache;
import org.platform.modules.abstr.storage.data.EmptyInstance;
import org.platform.utils.concurrent.NamedThreadFactory;
import org.platform.utils.serde.SerializerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

/**
 * REDIS存储
 */
public abstract class RedisCache implements ICache {

	private static final Logger LOG = LoggerFactory.getLogger(RedisCache.class);

	public static final int CACHE_RETRY_COUNT = 3;

	public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
			Runtime.getRuntime().availableProcessors(), 
			Runtime.getRuntime().availableProcessors() + 5, 
			1, TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(100), 
			new NamedThreadFactory("RedisCache"),
			new ThreadPoolExecutor.CallerRunsPolicy());

	@Resource(name = "persistenceJedisPool")
	protected ShardedJedisPoolWrapper shardedJedisPoolWrapper = null;

	protected ShardedJedisPool pool = null;

	private BeanUtilsBean beanUtilsBean = null;

	@PostConstruct
	public void postConstruct() {
		this.pool = shardedJedisPoolWrapper.getJedisPool();
		this.beanUtilsBean = BeanUtilsBean.getInstance();
	}
	
	public ShardedJedisPool getPool() {
		return pool;
	}

	/**
	 * transaction cache operation
	 * @param key
	 * @throws java.io.IOException
	 */
	public void transaction(String key, CacheOperationFactory factory) throws IOException {
		boolean isFailed = false;
		ShardedJedis jedis = pool.getResource();
		try {
			Jedis shard = jedis.getShard(key);
			shard.watch(SafeEncoder.encode(key));
			Transaction t = shard.multi();
			factory.setJedis(shard);
			factory.cache();
			t.exec();
		} catch (JedisException e) {
			isFailed = true;
			throw e;
		} finally {
			if (isFailed) {
				pool.returnBrokenResource(jedis);
			} else {
				pool.returnResource(jedis);
			}
		}
	}
	
	@Override
	public String set(final String key, final Object value) throws IOException {
		return new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
			public String cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.set(SafeEncoder.encode(key), SerializerUtil.write(value));
			}
		}.execute();
	}

	@Override
	public List<String> set(final String[] keys, final Object[] values) throws IOException {
		if (keys.length != values.length || keys.length == 0) {
			return null;
		}
		return new CacheOperation<List<String>>(CACHE_RETRY_COUNT) {
			public List<String> cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				List<String> results = new ArrayList<String>(keys.length);
				ArrayList<Response<String>> responses = new ArrayList<Response<String>>(keys.length);
				ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
				for (int i = 0; i < keys.length; i++) {
					responses.add(pipeline.set(keys[i], SerializerUtil.write(values[i])));
				}
				pipeline.sync();
				for (Response<String> response : responses) {
					results.add(response.get());
				}
				return results;
			}
		}.execute();
	}
	
	@Override
	public String set(final String key, final Object value, final int expireTime) throws IOException {
		return new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
			public String cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.setex(SafeEncoder.encode(key), expireTime, SerializerUtil.write(value));
			}
		}.execute();
	}

	@Override
	public Long setnx(final String key, final Object o) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.setnx(SafeEncoder.encode(key), SerializerUtil.write(o));
			}
		}.execute();
	}

	@Override
	public void delete(final String key) throws IOException {
		new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.getShard(key).del(SafeEncoder.encode(key));
			}
		}.execute();
	}

	@Override
	public <T> T get(final String key) throws IOException {
		try {
			return new CacheOperation<T>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public T cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					byte[] a = jedis.get(SafeEncoder.encode(key));
					Object t = SerializerUtil.read(a);
					return (T) t;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when get a object, key: " + key, e);
			return null;
		}
	}

	public <T> T getSet(final String key, final String value) throws IOException {
		try {
			return new CacheOperation<T>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public T cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					byte[] result = jedis.getSet(SafeEncoder.encode(key), SafeEncoder.encode(value));
					Object t = SerializerUtil.read(result);
					return (T) t;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when get a object, key: " + key, e);
			return null;
		}
	}

	@Override
	public <T> List<T> get(final String[] keys) throws IOException {
		try {
			return new CacheOperation<List<T>>(CACHE_RETRY_COUNT) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public List<T> cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					List<T> ts = new ArrayList<T>(keys.length);
					List<Response<byte[]>> responses = new ArrayList<Response<byte[]>>(keys.length);
					ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
					for (String key : keys) {
						responses.add(pipeline.getByte(key));
					}
					pipeline.sync();
					for (Response response : responses) {
						T t = (T) SerializerUtil.read((byte[]) response.get());
						ts.add(t);
					}
					return ts;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when get a object, keys: " + keys, e);
			return null;
		}
	}

	@Override
	public Long incrby(final String key, final long increment) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.incrBy(SafeEncoder.encode(key), increment);
			}
		}.execute();
	}

	@Override
	public Long decrby(final String key, final long increment) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.decrBy(SafeEncoder.encode(key), increment);
			}
		}.execute();
	}

	public void listTrim(final String key, final int start, final int end) throws IOException {
		new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				jedis.ltrim(key, start, end);
				return 0L;
			}
		}.execute();
	}
	
	@Override
	public <T> long listAppend(final String key, final T value) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				if (value == null) {
					return 0L;
				}
				byte[] bytes = SerializerUtil.write(value);
				return jedis.lpush(SafeEncoder.encode(key), bytes);
			}
		}.execute();
	}

	@Override
	public <T> long listAppend(final String key, final T[] values) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				if (values == null || values.length == 0) {
					return 0L;
				}
				byte[][] bytes = new byte[values.length][];
				int lastIndex = bytes.length - 1;
				for (int i = lastIndex; i >= 0; i--) {
					bytes[lastIndex - i] = SerializerUtil.write(values[i]);
				}
				return jedis.lpush(SafeEncoder.encode(key), bytes);
			}
		}.execute();
	}

	@Override
	public <T> T rPop(final String key) throws IOException {
		return new CacheOperation<T>(CACHE_RETRY_COUNT, key) {
			@SuppressWarnings("unchecked")
			public T cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				byte[] bytes = jedis.rpop(SafeEncoder.encode(key));
				if (bytes == null) {
					return null;
				}
				return (T) SerializerUtil.read(bytes);
			}
		}.execute();
	}
	
	@Override
	public <T> void listDel(final String key, final T value) throws IOException {
		new CacheOperation<Void>(CACHE_RETRY_COUNT, key) {
			public Void cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				if (value == null) {
					return null;
				}
				jedis.lrem(SafeEncoder.encode(key), 1, SerializerUtil.write(value));
				return null;
			}
		}.execute();
	}

	@Override
	public <T> void listDel(final String key, final T[] values) throws IOException {
		new CacheOperation<Void>(CACHE_RETRY_COUNT, key) {
			public Void cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				if (values == null || values.length == 0) {
					return null;
				}
				byte[][] bytes = new byte[values.length][];
				int lastIndex = bytes.length - 1;
				for (int i = lastIndex; i >= 0; i--) {
					jedis.lrem(SafeEncoder.encode(key), 1, SerializerUtil.write(values[i]));
				}
				return null;
			}
		}.execute();
	}

	@Override
	public Long listLength(final String key) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				try {
					return jedis.llen(SafeEncoder.encode(key));
				} catch (Exception e) {
					LOG.error("cache error when calling listlength, key=" + key, e);
					return 0L;
				}
			}
		}.execute();
	}

	@Override
	public <T> List<T> listRange(final String key, final int start, final int end) throws IOException {
		final int includeEnd = end - 1;
		if (start < 0 || includeEnd < start) {
			return Collections.emptyList();
		}
		final List<T> objs = new ArrayList<T>(includeEnd - start + 1);
		try {
			return new CacheOperation<List<T>>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public List<T> cacheExecute(ShardedJedis jedis) throws IOException {
					List<byte[]> values = jedis.lrange(SafeEncoder.encode(key), start, includeEnd);
					for (byte[] v : values) {
						try {
							objs.add((T) SerializerUtil.read(v));
						} catch (Exception e) {
							throw new JedisException(e);
						}
					}
					return objs;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling listRange, key: " + key, e);
			return objs;
		}
	}
	
	@Override
	public long zAdd(final String key, final double score, final Object value) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zadd(SafeEncoder.encode(key), score, SerializerUtil.write(value));
			}
		}.execute();
	}

	@Override
	public List<Long> zAdd(final String[] keys, final Double[] scores, final Object[] values) throws IOException {
		if (keys.length != values.length || keys.length != values.length || keys.length == 0) {
			return null;
		}
		try {
			return new CacheOperation<List<Long>>(CACHE_RETRY_COUNT) {
				@SuppressWarnings("rawtypes")
				public List<Long> cacheExecute(ShardedJedis jedis) throws IOException {
					List<Response<Long>> responses = new ArrayList<Response<Long>>(keys.length);
					ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
					for (int i = 0; i < keys.length; i++) {
						responses.add(pipeline.zadd(keys[i], scores[i], SerializerUtil.write(values[i])));
					}
					pipeline.sync();
					List<Long> results = new ArrayList<Long>(keys.length);
					for (Response response : responses) {
						results.add((Long) response.get());
					}
					return results;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when zAdd, keys: " + keys, e);
			return null;
		}
	}
	
	@Override
	public long zAdd(String key, double score, Object value, long len) throws IOException {
		long rt = zAdd(key, score, value);
		ShardedJedis jedis = null;
		boolean isFailed = false;
		try {
			jedis = pool.getResource();
			long total = jedis.zcard(SafeEncoder.encode(key));
			if (total > len) {
				jedis.zremrangeByRank(SafeEncoder.encode(key), 0, (int) (total- len - 1));
			}
		} catch (JedisException e) {
			LOG.error("exception occur when calling zcard, key: " + key, e);
			isFailed = true;
		} finally {
			if (jedis != null) {
				if (isFailed) {
					pool.returnBrokenResource(jedis);
				} else {
					pool.returnResource(jedis);
				}
			}
		}
		return rt;
	}

	@Override
	public long zAdd(final String key, final Map<Double, Object> map, long len) throws IOException {
		long rt = new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				Map<Double, byte[]> resultMap = new HashMap<Double, byte[]>();
				for (Double mkey : map.keySet()) {
					resultMap.put(mkey, SerializerUtil.write(map.get(mkey)));
				}
				return jedis.zadd(SafeEncoder.encode(key), resultMap);
			}
		}.execute();
		ShardedJedis jedis = null;
		boolean isFailed = false;
		try {
			jedis = pool.getResource();
			long total = jedis.zcard(SafeEncoder.encode(key));
			if (total > len) {
				jedis.zremrangeByRank(SafeEncoder.encode(key), 0, (int) (total - len - 1));
			}
		} catch (JedisException e) {
			LOG.error("exception occur when calling zcard, key: " + key, e);
			isFailed = true;
		} finally {
			if (jedis != null) {
				if (isFailed) {
					pool.returnBrokenResource(jedis);
				} else {
					pool.returnResource(jedis);
				}
			}
		}
		return rt;
	}
	
	/**
	 * 以Map<Object, Double>的形式批量设置redis缓存，object-member、Double-score，
	 * 当score值有重复时，调用该方法可以避免调用zAdd时map的key冲突
	 * 注意：当score值大量重复时，循环中会有很多冗余判断，会影响性能 数据太大时，redis处理会失败
	 */
	@Override
	public long zAddWithMap(final String key, final Map<Object, Double> map, long len) throws IOException {
		long rt = new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				Long addedNum = 0l;
				try {
					while (map.size() > 0) {
						Map<Double, byte[]> serMap = new HashMap<Double, byte[]>();
						Set<Object> keySet = new HashSet<Object>(map.keySet());
						int mapSize = 0;
						for (Object mkey : keySet) {
							if (!serMap.containsKey(map.get(mkey))) {
								serMap.put(map.remove(mkey), SerializerUtil.write(mkey));
								mapSize++;
							}
							if (mapSize >= 10000) {
								break;
							}
						}
						addedNum += jedis.zadd(SafeEncoder.encode(key), serMap);
					}
					// 如果catch异常，删掉key, 因为可能只加了一半, 异常继续往外抛
				} catch (IOException e) {
					jedis.del(key);
					LOG.warn("Exception when zAddWithMap, key=" + key);
					throw e;
				} catch (JedisException e) {
					jedis.del(key);
					LOG.warn("Exception when zAddWithMap, key=" + key);
					throw e;
				}
				return addedNum;
			}
		}.execute();
		ShardedJedis jedis = null;
		boolean isFailed = false;
		try {
			jedis = pool.getResource();
			long total = jedis.zcard(SafeEncoder.encode(key));
			if (total > len) {
				jedis.zremrangeByRank(SafeEncoder.encode(key), 0, (int) (total - len - 1));
			}
		} catch (JedisException e) {
			LOG.error("exception occur when calling zcard, key: " + key, e);
			isFailed = true;
		} finally {
			if (jedis != null) {
				if (isFailed) {
					pool.returnBrokenResource(jedis);
				} else {
					pool.returnResource(jedis);
				}
			}
		}
		return rt;
	}
	
	@Override
	public Double zIncrby(final String key, final long increment, final Object member) throws IOException {
		return new CacheOperation<Double>(CACHE_RETRY_COUNT, key) {
			public Double cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zincrby(SafeEncoder.encode(key), increment, SerializerUtil.write(member));
			}
		}.execute();
	}

	@Override
	public Double zScore(final String key, final Object value) throws IOException {
		try {
			return new CacheOperation<Double>(CACHE_RETRY_COUNT, key) {
				public Double cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					return jedis.zscore(SafeEncoder.encode(key), SerializerUtil.write(value));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zScore, key: " + key, e);
			return null;
		}
	}

	@Override
	public long zCard(final String key) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zcard(SafeEncoder.encode(key));
			}
		}.execute();
	}

	@Override
	public long zCount(final String key, final double min, final double max) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zcount(SafeEncoder.encode(key), min, max);
			}
		}.execute();
	}

	@Override
	public long zRemByValue(final String key, final Object value) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zrem(SafeEncoder.encode(key), SerializerUtil.write(value));
			}
		}.execute();
	}

	@Override
	public List<Long> zRemByValue(final String[] keys, final Object[] values) throws IOException {
		if (keys.length != values.length || keys.length != values.length || keys.length == 0) {
			return null;
		}
		try {
			return new CacheOperation<List<Long>>(CACHE_RETRY_COUNT) {
				@SuppressWarnings("rawtypes")
				public List<Long> cacheExecute(ShardedJedis jedis) throws IOException {
					List<Response<Long>> responses = new ArrayList<Response<Long>>(keys.length);
					ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
					for (int i = 0; i < keys.length; i++) {
						responses.add(pipeline.zrem(keys[i], SerializerUtil.write(values[i])));
					}
					pipeline.sync();
					List<Long> res = new ArrayList<Long>(keys.length);
					for (Response response : responses) {
						res.add((Long) response.get());
					}
					return res;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when zAdd, keys: " + keys, e);
			return null;
		}
	}

	@Override
	public long zRemRangeByRank(final String key, final int start, final int end) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zremrangeByRank(SafeEncoder.encode(key), start, end);
			}
		}.execute();
	}

	@Override
	public long zRemRangeByScore(final String key, final double start, final double end) throws IOException {
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.zremrangeByScore(SafeEncoder.encode(key), start, end);
			}
		}.execute();
	}

	@Override
	public <T> Set<T> zRevRange(final String key, final int start, final int end) throws IOException {
		final Set<T> objs = new LinkedHashSet<T>();
		if (start < 0 || end < start) {
			return objs;
		}
		try {
			return new CacheOperation<Set<T>>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public Set<T> cacheExecute(ShardedJedis jedis) throws IOException {
					Set<byte[]> rt = jedis.zrevrange(SafeEncoder.encode(key), start, end);
					for (byte[] it : rt) {
						try {
							objs.add((T) SerializerUtil.read(it));
						} catch (Exception e) {
							throw new JedisException(e);
						}
					}
					return objs;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zRevRange, key: " + key, e);
			return objs;
		}
	}

	@Override
	public <T> Set<T> zRevRangeByOffset(final String key, final int start, final int end) throws IOException {
		final Set<T> objs = new LinkedHashSet<T>();
		if (start < 0 || end < start) {
			return objs;
		}
		try {
			return new CacheOperation<Set<T>>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public Set<T> cacheExecute(ShardedJedis jedis) throws IOException {
					Set<byte[]> rt = jedis.zrevrange(SafeEncoder.encode(key), start, end - 1);
					for (byte[] it : rt) {
						try {
							objs.add((T) SerializerUtil.read(it));
						} catch (Exception e) {
							throw new JedisException(e);
						}
					}
					return objs;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when zRangeByScore, key: " + key, e);
			return objs;
		}
	}

	@Override
	public Set<Tuple> zRevWithScores(final String key, final int start, final int end) throws IOException {
		try {
			return new CacheOperation<Set<Tuple>>(CACHE_RETRY_COUNT, key) {
				public Set<Tuple> cacheExecute(ShardedJedis jedis) throws IOException {
					return jedis.zrevrangeWithScores(SafeEncoder.encode(key), start, end);
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zRangeByScore, key: " + key, e);
			return new LinkedHashSet<Tuple>();
		}
	}

	@Override
	public <T> Set<T> zRangeByScore(final String key, final double min, final double max, 
			final boolean rev) throws IOException {
		final Set<T> rt = new LinkedHashSet<T>();
		try {
			return new CacheOperation<Set<T>>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public Set<T> cacheExecute(ShardedJedis jedis) throws IOException {
					Set<byte[]> ranges;
					if (!rev) {
						ranges = jedis.zrangeByScore(SafeEncoder.encode(key), min, max);
					} else {
						ranges = jedis.zrevrangeByScore(SafeEncoder.encode(key), max, min);
					}
					for (byte[] range : ranges) {
						try {
							rt.add((T) SerializerUtil.read(range));
						} catch (Exception e) {
							throw new JedisException(e);
						}
					}
					return rt;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zRangeByScore, key: " + key, e);
			return rt;
		}
	}

	@Override
	public <T> Set<T> zRangeByScore(final String key, final double min, final double max, 
			final boolean rev, final int offset, final int length) throws IOException {
		final Set<T> rt = new LinkedHashSet<T>();
		try {
			return new CacheOperation<Set<T>>(CACHE_RETRY_COUNT, key) {
				@SuppressWarnings("unchecked")
				public Set<T> cacheExecute(ShardedJedis jedis) throws IOException {
					Set<byte[]> ranges;
					if (!rev) {
						ranges = jedis.zrangeByScore(SafeEncoder.encode(key), min, max, offset, length);
					} else {
						ranges = jedis.zrevrangeByScore(SafeEncoder.encode(key), max, min, offset, length);
						Set<Tuple> tuples = jedis.zrevrangeByScoreWithScores(
								SafeEncoder.encode(key), min, max, offset, length);
						for (Tuple tuple : tuples) {
							LOG.info("{} - {}", SerializerUtil.read(tuple.getBinaryElement()), tuple.getScore());
						}
					}
					for (byte[] range : ranges) {
						try {
							rt.add((T) SerializerUtil.read(range));
						} catch (Exception e) {
							throw new JedisException(e);
						}
					}
					return rt;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zRangeByScore, key: " + key, e);
			return rt;
		}
	}

	@Override
	public Set<Tuple> zRangeByScoreWithScores(final String key, final double min, final double max, 
			final boolean rev, final int offset, final int length) throws IOException {
		try {
			return new CacheOperation<Set<Tuple>>(CACHE_RETRY_COUNT, key) {
				public Set<Tuple> cacheExecute(ShardedJedis jedis) throws IOException {
					if (!rev) {
						return jedis.zrangeByScoreWithScores(SafeEncoder.encode(key), min, max, offset, length);
					} else {
						return jedis.zrevrangeByScoreWithScores(SafeEncoder.encode(key), max, min, offset, length);
					}
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zRangeByScoreWithScores, key: " + key, e);
			return new LinkedHashSet<Tuple>();
		}
	}

	@Override
	public List<Set<Tuple>> zRangeByScoreWithScores(final String[] keys, final double min, final double max, 
			final boolean rev, final int offset, final int length) throws IOException {
		if (keys.length == 0) {
			return null;
		}
		try {
			return new CacheOperation<List<Set<Tuple>>>(CACHE_RETRY_COUNT) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public List<Set<Tuple>> cacheExecute(ShardedJedis jedis) throws IOException {
					List<Response<Set<Tuple>>> responses = new ArrayList<Response<Set<Tuple>>>(keys.length);
					ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
					if (!rev) {
						for (String key : keys) {
							responses.add(pipeline.zrangeByScoreWithScores(key, min, max, offset, length));
						}
					} else {
						for (String key : keys) {
							responses.add(pipeline.zrevrangeByScoreWithScores(key, max, min, offset, length));
						}
					}
					pipeline.sync();
					List<Set<Tuple>> res = new ArrayList<Set<Tuple>>(keys.length);
					for (Response response : responses) {
						res.add((Set<Tuple>) response.get());
					}
					return res;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when zRangeByScoreWithScores, keys: " + keys, e);
			return null;
		}
	}

	@Override
	public List<Tuple> zMergeRangeByScoreWithScores(final String[] keys, final double min, final double max, 
			final boolean rev, final int offset, final int length) throws IOException {
		List<Set<Tuple>> sets = zRangeByScoreWithScores(keys, min, max, rev, offset, length);
		if (CollectionUtils.isEmpty(sets)) {
			LOG.info("empty result when zMergeRangeByScoreWithScores keys=" + keys);
			return null;
		}
		List<Tuple> tuples = new ArrayList<Tuple>();
		for (Set<Tuple> set : sets) {
			if (CollectionUtils.isEmpty(set)) {
				continue;
			}
			for (Tuple tuple : set) {
				tuples.add(tuple);
			}
		}
		if (rev) {
			Collections.sort(tuples, Collections.reverseOrder());
		} else {
			Collections.sort(tuples);
		}
		if (tuples.size() <= length) {
			return tuples;
		}
		return tuples.subList(0, length);
	}

	@Override
	public Void hmSet(final String[] keys, final Object[] values, final int expireTime) throws IOException {
		if (keys.length != values.length || keys.length == 0) {
			return null;
		}
		final List<Map<byte[], byte[]>> maps = new ArrayList<Map<byte[], byte[]>>();
		for (Object o : values) {
			if (o != null) {
				maps.add(parseObject2Map(o));
			}
		}
		return new CacheOperation<Void>(CACHE_RETRY_COUNT) {
			public Void cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
				int validCount = 0;
				for (int i = 0; i < keys.length; i++) {
					if (values[i] != null) {
						pipeline.hmsetByte(keys[i], maps.get(validCount++));
						if (expireTime > 0) {
							pipeline.expire(keys[i], expireTime);
						}
					}
				}
				pipeline.sync();
				return null;
			}
		}.execute();
	}

	@Override
	public Void hmSet(final String[] keys, final Object[] os) throws IOException {
		return hmSet(keys, os, -1);
	}

	@Override
	public Void hmSet(final String key, final Object o, final int expireTime) throws IOException {
		final Map<byte[], byte[]> map = parseObject2Map(o);
		return new CacheOperation<Void>(CACHE_RETRY_COUNT, key) {
			public Void cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
				pipeline.hmsetByte(key, map);
				if (expireTime > 0) {
					pipeline.expire(key, expireTime);
				}
				pipeline.sync();
				return null;
			}
		}.execute();
	}

	@Override
	public String hmSet(final String key, final Object o) throws IOException {
		final Map<byte[], byte[]> map = parseObject2Map(o);
		return new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
			public String cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
				return jedis.hmset(SafeEncoder.encode(key), map);
			}
		}.execute();
	}

	@Override
	public void hmSet(final String key, final Map<String, Object> map) throws IOException {
		if (hExists(key)) {
			new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
				public String cacheExecute(ShardedJedis jedis) throws IOException {
					if (map == null || map.isEmpty()) {
						return null;
					}
					Map<byte[], byte[]> binaryMap = new HashMap<byte[], byte[]>(map.size());
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						binaryMap.put(SerializerUtil.write(entry.getKey()), 
								SerializerUtil.write(entry.getValue()));
					}
					return jedis.hmset(SafeEncoder.encode(key), binaryMap);
				}
			}.execute();
		}
	}

	@Override
	public void hmSetCreateKeyIfNotExists(final String key, final Map<String, Object> map) throws IOException {
		new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
			public String cacheExecute(ShardedJedis jedis) throws IOException {
				if (map == null || map.isEmpty()) {
					return null;
				}
				Map<byte[], byte[]> binaryMap = new HashMap<byte[], byte[]>(map.size());
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					binaryMap.put(SerializerUtil.write(entry.getKey()),
							SerializerUtil.write(entry.getValue()));
				}
				return jedis.hmset(SafeEncoder.encode(key), binaryMap);
			}
		}.execute();
	}

	@Override
	public void hmSetMap(final String key, final Map<String, String> map) throws IOException {
		new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
			public String cacheExecute(ShardedJedis jedis) throws IOException {
				if (map == null || map.isEmpty()) {
					return null;
				}
				Map<byte[], byte[]> binaryMap = new HashMap<byte[], byte[]>(map.size());
				for (Map.Entry<String, String> entry : map.entrySet()) {
					binaryMap.put(SerializerUtil.write(entry.getKey()),
							SerializerUtil.write(entry.getValue()));
				}
				return jedis.hmset(SafeEncoder.encode(key), binaryMap);
			}
		}.execute();
	}

	@Override
	public Map<String, String> hmGetMap(final String key) throws IOException {
		try {
			return new CacheOperation<Map<String, String>>(CACHE_RETRY_COUNT, key) {
				public Map<String, String> cacheExecute(ShardedJedis jedis) throws IOException {
					Map<byte[], byte[]> all = jedis.hgetAll(SafeEncoder.encode(key));
					Map<String, String> map = new HashMap<String, String>();
					for (byte[] key : all.keySet()) {
						String field = (String) SerializerUtil.read(key);
						String value = String.valueOf(SerializerUtil.read(all.get(key)));
						map.put(field, value);
					}
					return map;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hmGetMap, key: " + key, e);
			return null;
		}
	}

	/**
	 * 慎用： key不存在时不会创建key 导致操作无效
	 */
	@Override
	public long hSet(final String key, final String field, final String value) throws IOException {
		if (hExists(key)) {
			try {
				return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
					public Long cacheExecute(ShardedJedis jedis) throws IOException {
						return jedis.hset(SafeEncoder.encode(key),
								SerializerUtil.write(field), SerializerUtil.write(value));
					}
				}.execute();
			} catch (JedisException e) {
				LOG.error("exception occur when calling hSet, key: " + key, e);
				return 0L;
			}
		}
		return 0L;
	}

	/**
	 * 慎用： key不存在时不会创建key 导致操作无效
	 */
	@Override
	public long hSet(final String key, final String field, final Object object) throws IOException {
		if (hExists(key)) {
			try {
				return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
					public Long cacheExecute(ShardedJedis jedis) throws IOException {
						return jedis.hset(SafeEncoder.encode(key),
								SerializerUtil.write(field), SerializerUtil.write(object));
					}
				}.execute();
			} catch (JedisException e) {
				LOG.error("exception occur when calling hSet, key: " + key, e);
				return 0L;
			}
		}
		return 0L;
	}

	/**
	 * 慎用： key不存在时不会创建key 导致操作无效
	 */
	@Override
	public long hSetCreateKeyIfNotExist(final String key, final String field,
			final Object object) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					return jedis.hset(SafeEncoder.encode(key),
							SerializerUtil.write(field), SerializerUtil.write(object));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hSet, key: " + key, e);
			return 0L;
		}
	}

	public long hDel(final String key, final String field) throws IOException {
		if (hExists(key)) {
			try {
				return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
					public Long cacheExecute(ShardedJedis jedis) throws IOException {
						return jedis.hdel(SafeEncoder.encode(key), SerializerUtil.write(field));
					}
				}.execute();
			} catch (JedisException e) {
				LOG.error("exception occur when calling hSet, key: " + key, e);
				return 0L;
			}
		}
		return 0L;
	}

	@Override
	public long hSetCreateKeyIfNotExists(final String key, final String field,
			final String value) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					return jedis.hset(SafeEncoder.encode(key),
							SerializerUtil.write(field), SerializerUtil.write(value));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when hExists a object, key: " + key, e);
			return 0L;
		}
	}

	@Override
	public String hGet(final String key, final String field) throws IOException {
		try {
			return new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
				public String cacheExecute(ShardedJedis jedis) throws IOException {
					return (String) SerializerUtil.read(jedis.hget(
							SafeEncoder.encode(key), SerializerUtil.write(field)));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hGet, key: " + key, e);
			return null;
		}
	}

	public Object hGetObject(final String key, final String field) throws IOException {
		try {
			return new CacheOperation<Object>(CACHE_RETRY_COUNT, key) {
				public Object cacheExecute(ShardedJedis jedis) throws IOException {
					return SerializerUtil.read(jedis.hget(
							SafeEncoder.encode(key), SerializerUtil.write(field)));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hGet, key: " + key, e);
			return null;
		}
	}

	@Override
	public boolean hExists(final String key) throws IOException {
		try {
			return new CacheOperation<Boolean>(CACHE_RETRY_COUNT, key) {
				public Boolean cacheExecute(ShardedJedis jedis) throws IOException {
					long len = jedis.hlen(SafeEncoder.encode(key));
					return len > 0 ? true : false;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hExists, key: " + key, e);
			return false;
		}
	}

	public long hLen(final String key) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					return jedis.hlen(SafeEncoder.encode(key));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hExists, key: " + key, e);
			return 0l;
		}
	}

	/**
	 * remove a object in hash. remove in a transaction, if one field not delete success, then rollback.
	 * @param key
	 * @return
	 * @throws java.io.IOException
	 */
	@Override
	public long hDel(final String key) throws JedisException, IOException {
		if (key == null || key.trim().length() == 0) {
			throw new JedisException("no key is null or length of o");
		}
		return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
			public Long cacheExecute(ShardedJedis jedis) throws IOException {
				byte[] byteKey = SafeEncoder.encode(key);
				Set<byte[]> fields = jedis.hkeys(byteKey);
				if (fields == null || fields.size() == 0) {
					return 0L;
				}
				return jedis.hdel(byteKey, fields.toArray(new byte[][] {}));
			}
		}.execute();
	}

	@Override
	public Long hIncrBy(final String key, final String field, final int increment)
			throws JedisException, IOException {
		return hIncrBy(key, field, increment, EmptyInstance.LONG);
	}

	@Override
	public Long hIncrBy(final String key, final String field, final int increment, 
			final long defaultValue) throws JedisException, IOException {
		boolean hExists = hExists(key);
		if (hExists) {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					byte[] bkey = SafeEncoder.encode(key);
					byte[] bfield = SerializerUtil.write(field);
					Jedis shard = jedis.getShard(bkey);
					try {
						shard.watch(bkey);
						String wantedValue;
						Object filedValue = SerializerUtil.read(jedis.hget(bkey, bfield));
						if (filedValue != null) {
							if (filedValue instanceof Integer) {
								wantedValue = String.valueOf(((Integer) filedValue) + increment);
							} else if (filedValue instanceof Long) {
								wantedValue = String.valueOf(((Long) filedValue) + increment);
							} else if (filedValue instanceof String) {
								String filedValueStr = (String) filedValue;
								if (org.apache.commons.lang.StringUtils.isNotEmpty(filedValueStr)) {
									wantedValue = String.valueOf(Long.parseLong(filedValueStr) + increment);
								} else {
									wantedValue = String.valueOf(defaultValue + increment);
								}
							} else {
								wantedValue = String.valueOf(defaultValue + increment);
							}
						} else {
							wantedValue = String.valueOf(defaultValue + increment);
						}
						Transaction t = shard.multi();
						Response<Long> rt = t.hset(bkey, bfield, SerializerUtil.write(wantedValue));
						List<Object> list = t.exec();
						if (list == null) {
							shard.unwatch();
							throw new JedisDataException("Transaction failed");
						}
						for (Object obj : list) {
							if (obj instanceof JedisException) {
								throw (JedisException) obj;
							}
						}
						return rt.get();
					} catch (Exception e) {
						shard.unwatch();
						throw new JedisException(e);
					}
				}
			}.execute();
		} else {
			return 0L;
		}
	}

	@Override
	public Object hmGet(final String key, final Class<?> clazz) throws IOException {
		try {
			Map<byte[], byte[]> map = new CacheOperation<Map<byte[], byte[]>>(CACHE_RETRY_COUNT, key) {
				public Map<byte[], byte[]> cacheExecute(ShardedJedis jedis) throws IOException {
					Map<byte[], byte[]> all = jedis.hgetAll(SafeEncoder.encode(key));
					return all;
				}
			}.execute();
			return parseObjFromMap(map, clazz);
		} catch (JedisException e) {
			LOG.error("exception occur when hmGet a object", e);
			return null;
		}
	}

	@Override
	public <T> List<T> hmGet(final String[] keys, final Class<?> clazz) throws IOException {
		try {
			return new CacheOperation<List<T>>(CACHE_RETRY_COUNT) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public List<T> cacheExecute(ShardedJedis jedis) throws IOException {
					List<T> objects = new ArrayList<T>(keys.length);
					List<Response<Map<byte[], byte[]>>> responses = new ArrayList<Response<Map<byte[], byte[]>>>(
							keys.length);
					ExtendedShardedJedisPipeline pipeline = new ExtendedShardedJedisPipeline(jedis);
					for (String key : keys) {
						responses.add(pipeline.hgetBytesAll(key));
					}
					pipeline.sync();
					Map<byte[], byte[]> all;
					for (Response response : responses) {
						all = (Map<byte[], byte[]>) response.get();
						objects.add((T) parseObjFromMap(all, clazz));
					}
					return objects;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hmGetMaps, keys: " + keys, e);
			return null;
		}
	}

	protected Map<byte[], byte[]> parseObject2Map(Object object) throws IOException {
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		Field[] fields = object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				boolean isStatic = Modifier.isStatic(field.getModifiers());// 静态变量不存缓存
				if (field != null && field.getName() != null
						&& field.getAnnotation(IgnoreCache.class) == null && !isStatic) {
					field.setAccessible(true);
					Object obj = field.get(object);
					if (obj == null) {
						continue;
					}
					if (obj instanceof BigDecimal) {
						map.put(SerializerUtil.write(field.getName()), SerializerUtil.write(obj));
					} else {
						String value = beanUtilsBean.getProperty(object, field.getName());
						map.put(SerializerUtil.write(field.getName()), SerializerUtil.write(value));
					}
				}
			}
		} catch (JedisException e) {
			LOG.error("Can't parse Object to Map", e);
		} catch (NoSuchMethodException e) {
			LOG.error("Can't parse Object to Map", e);
		} catch (InvocationTargetException e) {
			LOG.error("Can't parse Object to Map", e);
		} catch (IllegalAccessException e) {
			LOG.error("Can't parse Object to Map", e);
		}
		return map;
	}

	private static Map<String, Map<String, Class<?>>> classFields = new HashMap<String, Map<String, Class<?>>>();

	public Object parseObjFromMap(Map<byte[], byte[]> map, Class<?> clazz) throws IOException {
		Object o = null;
		String field;
		if (map == null || map.size() <= 0) {
			return o;
		}
		Map<String, Class<?>> fieldMap = null;
		synchronized (classFields) {
			fieldMap = classFields.get(clazz.getName());
			if (classFields.get(clazz.getName()) == null) {
				LOG.info(clazz.getName()
						+ " is not exists in classFields, will generate it and put to map for cache");
				Field[] fields = clazz.getDeclaredFields();
				fieldMap = new HashMap<String, Class<?>>();
				for (Field f : fields) {
					fieldMap.put(f.getName(), f.getType());
				}
				classFields.put(clazz.getName(), fieldMap);
			}
		}
		Map<String, Object> valueMap = new HashMap<String, Object>();
		try {
			o = clazz.newInstance();
			for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
				field = (String) SerializerUtil.read(entry.getKey());
				Object obj = SerializerUtil.read(entry.getValue());
				if (obj == null || field == null) {
					continue;
				}
				valueMap.put(field, obj);
			}
			BeanMap beanMap = BeanMap.create(o);
			beanMap.setBean(o);
			beanMap.putAll(valueMap);
		} catch (Exception e) {
			LOG.error("Can't parse Object from List", e);
			return null;
		}
		return o;
	}


	public Long total(final String key) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					Long total = jedis.zcard(SafeEncoder.encode(key));
					if (total == null) {
						return 0L;
					}
					return total;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling total, key: " + key, e);
			return 0L;
		}
	}

	public Long zRank(final String key, final Object target) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					return jedis.zrank(SafeEncoder.encode(key), SerializerUtil.write(target));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zRanking, key: " + key + ", value:" + target, e);
			return null;
		}
	}

	public Long zRevRank(final String key, final Object target) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					return jedis.zrevrank(SafeEncoder.encode(key), SerializerUtil.write(target));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zrevrank, key: " + key
					+ ", value:" + target.toString(), e);
			return null;
		}
	}

	/**
	 * 获取key的排名 如果列表不存在返回 -1 如果key不存在返回 null
	 * @param key
	 * @param target
	 * @return
	 * @throws java.io.IOException
	 */
	public Long zRevRankNoExpire(final String key, final Object target) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws JedisException, IOException {
					byte[] keys = SafeEncoder.encode(key);
					Transaction t = jedis.getShard(key).multi();
					Response<Long> rankResponse = t.zrevrank(keys, SerializerUtil.write(target));
					Response<Boolean> existResponse = t.exists(keys);
					t.exec();
					if (!existResponse.get()) {
						return -1L;
					} else {
						return rankResponse.get();
					}
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling zrevrank, key: " + key
					+ ", value:" + target.toString(), e);
			return null;
		}
	}

	public boolean expire(final String key, final int seconds) throws IOException {
		try {
			return new CacheOperation<Boolean>(CACHE_RETRY_COUNT, key) {
				public Boolean cacheExecute(ShardedJedis jedis) throws IOException {
					Long expired = jedis.expire(key, seconds);
					return expired != null;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling expire, key: " + key, e);
			return false;
		}
	}

	/**
	 * exists checking for the key with the expired command
	 */
	public boolean exists(final String key) throws IOException {
		try {
			return new CacheOperation<Boolean>(CACHE_RETRY_COUNT, key) {
				public Boolean cacheExecute(ShardedJedis jedis) throws IOException {
					Long ttl = jedis.ttl(key);
					if (ttl == -1) {// non-expire key or non-exists key
						Boolean exists = jedis.exists(key);
						return exists != null && exists.booleanValue();
					} else if (ttl < 1) {
						try {
							delete(key);
						} catch (Exception e) {
							LOG.error("delete key error! " + key, e);
						}
						return false;
					}
					return true;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling exists, key: " + key, e);
			return false;
		}
	}

	@Override
	public void hmSetForPush(final String key, final Map<Long, Object> map) throws IOException {
		try {
			new CacheOperation<String>(CACHE_RETRY_COUNT, key) {
				public String cacheExecute(ShardedJedis jedis) throws IOException {
					if (map == null || map.isEmpty()) {
						return null;
					}
					Map<byte[], byte[]> binaryMap = new HashMap<byte[], byte[]>(map.size());
					for (Map.Entry<Long, Object> entry : map.entrySet()) {
						binaryMap.put(SerializerUtil.write(entry.getKey()),
								SerializerUtil.write(entry.getValue()));
					}
					return jedis.hmset(SafeEncoder.encode(key), binaryMap);
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hmSetForPush, key: " + key, e);
		}
	}

	@Override
	public Object hmGetForPush(final String key) throws IOException {
		try {
			return new CacheOperation<Object>(CACHE_RETRY_COUNT, key) {
				public Object cacheExecute(ShardedJedis jedis) throws IOException {
					Map<byte[], byte[]> binaryMap = jedis.hgetAll(SafeEncoder.encode(key));
					Map<Long, Object> resultMap = new HashMap<Long, Object>();
					for (Map.Entry<byte[], byte[]> entry : binaryMap.entrySet()) {
						resultMap.put((Long) ConvertUtils.convert(
								SerializerUtil.read(entry.getKey()), Long.class),
								SerializerUtil.read(entry.getValue()));
					}
					return resultMap;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling calling hmGetForPush, key:" + key, e);
			return null;
		}
	}

	public long hmDel(final String key, final long mapKey) throws IOException {
		if (key == null || key.trim().length() == 0) {
			throw new JedisException("no key is null or length of o");
		}
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					return jedis.hdel(SafeEncoder.encode(key), SerializerUtil.write(mapKey));
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling hmDel, key: " + key, e);
			return -1L;
		}
	}

	public boolean setsIsMember(final String key, final String member) throws IOException {
		try {
			return new CacheOperation<Boolean>(CACHE_RETRY_COUNT, key) {
				public Boolean cacheExecute(ShardedJedis jedis) throws IOException {
					Boolean exists = jedis.sismember(key, member);
					return exists != null && exists.booleanValue();
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling setsIsMember, key: " + key, e);
			return false;
		}
	}

	public Long setsAddMember(final String key, final String... member) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					Long added = jedis.sadd(key, member);
					return added;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling setsAddMember a object, key: " + key, e);
			return -1L;
		}
	}

	public Long setsDelMember(final String key, final String... member) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					Long removed = jedis.srem(key, member);
					return removed;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling setsDelMember, key: " + key, e);
			return -1L;
		}
	}

	public Set<String> setsAllMember(final String key) throws IOException {
		try {
			return new CacheOperation<Set<String>>(CACHE_RETRY_COUNT, key) {
				public Set<String> cacheExecute(ShardedJedis jedis) throws IOException {
					Set<String> members = jedis.smembers(key);
					return members;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling setsAllMember, key: " + key, e);
			return null;
		}
	}

	/**
	 * if non-exsist return 0 ,like the empty set
	 * @param key
	 * @return
	 * @throws java.io.IOException
	 */
	public long setsCountMember(final String key) throws IOException {
		try {
			return new CacheOperation<Long>(CACHE_RETRY_COUNT, key) {
				public Long cacheExecute(ShardedJedis jedis) throws IOException {
					Long count = jedis.scard(key);
					return count;
				}
			}.execute();
		} catch (JedisException e) {
			LOG.error("exception occur when calling setsCountMember, key: " + key, e);
			return 0;
		}
	}

	/**
	 * Cache lock.
	 * @param key
	 * @param seconds 任务间隔时间控制
	 * @return
	 */
	public boolean lock(String key, int seconds) {
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getPool().getResource();
			Jedis jedis = shardedJedis.getShard(key);
			if (jedis.exists(key)) {
				return false;
			}
			jedis.watch(key);
			Transaction transaction = jedis.multi();
			Response<Long> response = transaction.setnx(key, "0");
			transaction.expire(key, seconds);
			List<Object> list = transaction.exec();
			return list != null && response.get() > 0;
		} catch (JedisException e) {
			LOG.error("lock : " + key, e);
		} finally {
			if (shardedJedis != null) {
				getPool().returnResource(shardedJedis);
			}
		}
		return false;
	}

	/**
	 * release lock
	 * 谨慎使用 如果任务时间比lock字段存在时间久 释放的可能是其他任务的lock
	 * 一般情况下可不使用
	 * @param key
	 * @return
	 */
	public boolean releaseLock(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = this.getPool().getResource();
			return jedis.del(key) > 0;
		} catch (JedisException e) {
			LOG.error("releaseLock key : " + key, e);
		} finally {
			if (jedis != null) {
				this.getPool().returnResource(jedis);
			}
		}
		return false;
	}

	/**
	 * 批量操作zadd
	 */
	protected class BatchZAddOperator {

		private List<String> keys = null;

		private List<Double> scores = null;

		private List<Object> objects = null;

		public BatchZAddOperator() {
			this.keys = new ArrayList<String>();
			this.scores = new ArrayList<Double>();
			this.objects = new ArrayList<Object>();
		}

		public void add(String key, double score, Object object) {
			this.keys.add(key);
			this.scores.add(score);
			this.objects.add(object);
		}

		public String[] getKeys() {
			return keys.toArray(new String[0]);
		}

		public Double[] getScores() {
			return scores.toArray(new Double[0]);
		}

		public Object[] getObjects() {
			return objects.toArray(new Object[0]);
		}
	}
	
	protected abstract class CacheOperationFactory {
		
		private Jedis jedis = null;

		public void setJedis(Jedis jedis) {
			this.jedis = jedis;
		}

		public Jedis getJedis() {
			return this.jedis;
		}

		public abstract void cache() throws JedisException, IOException;
	}
	
	protected abstract class CacheOperation<T> {
		
		private int retryCount;
		
		private String key = null;
		
		public CacheOperation(int retryCount) {
			this.retryCount = retryCount;
		}

		public CacheOperation(int retryCount, String key) {
			this.retryCount = retryCount;
			this.key = key;
		}

		public T execute() throws JedisException, IOException {
			if (retryCount <= 0) {
				throw new JedisException("cache operate count less than 1");
			}
			while (retryCount > 0) {
				try {
					return cacheExecute();
				} catch (JedisException e) {
					LOG.error("cache CountOperation exception, count: " + retryCount, e);
					retryCount--;
				}
			}
			throw new JedisException("cache CountOperation exception count=" + retryCount);
		}

		private T cacheExecute() throws JedisException, IOException {
			ShardedJedis jedis = null;
			boolean isFailed = false;
			try {
				jedis = pool.getResource();
				return cacheExecute(jedis);
			} catch (JedisException e) {
				if (!(e.getCause() instanceof JedisDataException)) {
					isFailed = true;
					 if(jedis != null && key != null) {
	                        JedisShardInfo shardInfo = jedis.getShardInfo(key);
	                        LOG.error("JedisException@{}:{}", shardInfo.getHost(), shardInfo.getPort());
	                 }
				}
				throw e;
			} finally {
				if (jedis != null) {
					if (isFailed) {
						pool.returnBrokenResource(jedis);
					} else {
						pool.returnResource(jedis);
					}
				}
			}
		}

		public abstract T cacheExecute(ShardedJedis jedis) throws JedisException, IOException;
	}

}

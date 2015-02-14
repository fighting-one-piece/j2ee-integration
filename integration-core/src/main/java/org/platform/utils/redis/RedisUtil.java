package org.platform.utils.redis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.platform.utils.spring.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {

	private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);
	
	private static Map<Method, Method> cache = new HashMap<Method, Method>();

	/**
	 * 获得连接池
	 * @return
	 */
	public static ShardedJedisPool getPool() {
		return SpringContext.get("shardedJedisPool", ShardedJedisPool.class);
	}

	/**
	 * 返回ShardedJedis对象，该对象一直使用一个连接，注意最后调用完毕后需要自动释放连接。如下:
	 * @return
	 */
	public static ShardedJedis getJedis() {
		return getPool().getResource();
	}

	public static interface JedisAutoReturn extends JedisCommands, BinaryJedisCommands {
		Map<String, String> hgetAll(String key);
	}

	private static Method getMethod(Method method) throws SecurityException, NoSuchMethodException {
		Method cacheMethod = cache.get(method);
		if (null != cacheMethod) return cacheMethod;
		Class<ShardedJedis> shardedJedis = ShardedJedis.class;
		cacheMethod = shardedJedis.getMethod(method.getName(), method.getParameterTypes());
		cache.put(method, cacheMethod);
		return cacheMethod;
	}

	/**
	 * 返回的JedisAutoReturn对象，每次执行方法后都会自动释放连接
	 * @return
	 */
	public static JedisAutoReturn getJedisAutoReturn() {
		Object proxy = Proxy.newProxyInstance(ShardedJedisPool.class.getClassLoader(),
				new Class[] { JedisAutoReturn.class }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						ShardedJedisPool pool = getPool();
						ShardedJedis jedis = null;
						try {
							jedis = pool.getResource();
							if (JedisAutoReturn.class != method.getDeclaringClass()) {
								return method.invoke(jedis, args);
							} else {
								return getMethod(method).invoke(jedis, args);
							}
						} finally {
							pool.returnResource(jedis);
							LOG.info("Jedis Pool Return Resource");
						}
					}
				});
		return (JedisAutoReturn) proxy;
	}

}

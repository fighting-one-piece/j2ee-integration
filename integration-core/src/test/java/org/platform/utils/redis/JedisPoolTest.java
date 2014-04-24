/**
 * Aug 15, 2012
 */
package org.platform.utils.redis;

import static junit.framework.Assert.assertEquals;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @version 1.0
 * @since 1.0
 */
public class JedisPoolTest {
	private static JedisPool pool;

	@Before
	public void before() {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.valueOf(bundle
				.getString("redis.pool.maxActive")));
		config.setMaxIdle(Integer.valueOf(bundle
				.getString("redis.pool.maxIdle")));
		config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")));
		config.setTestOnBorrow(Boolean.valueOf(bundle
				.getString("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(bundle
				.getString("redis.pool.testOnReturn")));
		pool = new JedisPool(config, bundle.getString("redis.ip"),
				Integer.valueOf(bundle.getString("redis.port")));
	}

	@Test
	public void test() {
		// 从池中获取一个Jedis对象
		Jedis jedis = pool.getResource();
		String keys = "name";
		String value = "snowolf";
		// 删数据
		jedis.del(keys);
		// 存数据
		jedis.set(keys, value);
		// 取数据
		String v = jedis.get(keys);

		System.out.println(v);

		// 释放对象池
		pool.returnResource(jedis);

		assertEquals(value, v);
	}

}

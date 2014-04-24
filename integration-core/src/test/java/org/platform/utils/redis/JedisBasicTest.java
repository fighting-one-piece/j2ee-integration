package org.platform.utils.redis;

import redis.clients.jedis.Jedis;

public class JedisBasicTest {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.10.10");
		String keys = "name";
		// 删数据
		jedis.del(keys);
		// 存数据
		jedis.set(keys, "zhangsan");
		// 取数据
		String value = jedis.get(keys);
		System.out.println(value);
	}
}

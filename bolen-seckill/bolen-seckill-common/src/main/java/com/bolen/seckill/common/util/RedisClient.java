package com.bolen.seckill.common.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

	@Autowired
	JedisPool jedisPool;

	private static final Logger logger = Logger.getLogger(RedisClient.class);

	public String set(String key, String value) {
		String set = null;
		Jedis jedis = jedisPool.getResource();
		set = jedis.set(key, value);
		jedis.close();

		return set;
	}

	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}

	public Integer decrese(String key) {
		Jedis jedis = jedisPool.getResource();
		Long decr = jedis.decr(key);
		jedis.close();
		return decr.intValue();
	}

	public Integer increse(String key) {
		Jedis jedis = jedisPool.getResource();
		Long decr = jedis.incr(key);
		jedis.close();
		return decr.intValue();
	}

	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String hget = jedis.hget(key, field);
		jedis.close();
		return hget;
	}

	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long hset = jedis.hset(key, field, value);
		jedis.close();
		return hset;
	}

	public boolean sismember(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		Boolean sismember = jedis.sismember(key, member);
		jedis.close();
		return sismember;
	}

	public Long sadd(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		Long sadd = jedis.sadd(key, member);
		jedis.close();
		return sadd;
	}
	
	public Long srem(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		Long sadd = jedis.srem(key, member);
		jedis.close();
		return sadd;
	}
	
	public Long incrby(String key, Integer incre) {
		Jedis jedis = jedisPool.getResource();
	    Long incrBy = jedis.incrBy(key, incre);
		jedis.close();
		return incrBy;
	}
	
}

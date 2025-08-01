package com.example.ecommerce.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtils {
	
	public static boolean refreshRedisDataAll(String redisKey,Object genericList,RedisTemplate<String,Object> redisTemplate) {
		try {
		if(redisTemplate.hasKey(redisKey)==null) {
			redisTemplate.opsForValue().set(redisKey, genericList);
		}else {
			redisTemplate.delete(redisKey);
			redisTemplate.opsForValue().set(redisKey, genericList);
		}
		return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

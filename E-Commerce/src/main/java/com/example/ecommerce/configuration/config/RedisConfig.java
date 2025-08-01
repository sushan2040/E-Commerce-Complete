package com.example.ecommerce.configuration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisConfig {
	
	 @Value("${spring.redis.host}")
	    private String redisHost;

	    @Value("${spring.redis.port}")
	    private int redisPort;

	    @Value("${spring.redis.password}")
	    private String redisPassword;

	    @Value("${spring.redis.database}")
	    private int redisDatabase;

	    @Bean
	    public RedisConnectionFactory redisConnectionFactory() {
	        // LettuceConnectionFactory can handle password and host configurations
	        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
	        
	        // Set password if provided
	        if (redisPassword != null && !redisPassword.isEmpty()) {
	            lettuceConnectionFactory.setPassword(redisPassword);
	        }

	        // Set database if provided
	        lettuceConnectionFactory.setDatabase(redisDatabase);

	        return lettuceConnectionFactory;
	    }

	    @Bean
	    public RedisTemplate<String, Object> redisTemplate() {
	        RedisTemplate<String, Object> template = new RedisTemplate<>();
	        template.setConnectionFactory(redisConnectionFactory());
	        return template;
	    }
}

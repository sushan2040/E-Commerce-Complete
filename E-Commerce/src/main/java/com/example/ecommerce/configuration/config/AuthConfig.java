package com.example.ecommerce.configuration.config;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.configuration.service.UsersService;

@Configuration
public class AuthConfig {
	
	@Autowired
	UsersService usersService;

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	 @Bean
	    UserDetailsService userDetailsService() {
	        return username -> {
	        		Users userBean = null;
	        		if(redisTemplate.opsForValue().get(username)==null) {
					userBean = (Users)usersService.findByEmail(username);
					redisTemplate.opsForValue().set(username,userBean);
	        		}else {
	        			userBean=(Users)redisTemplate.opsForValue().get(username);
	        		}
	        		return userBean;
	        };
	    }

	    @Bean
	    BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }

	    @Bean
	    AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());

	        return authProvider;
	    }
	
}

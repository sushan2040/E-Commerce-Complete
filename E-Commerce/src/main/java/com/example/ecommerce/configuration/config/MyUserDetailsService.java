package com.example.ecommerce.configuration.config;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.ecommerce.configuration.masters.Users;
import com.example.ecommerce.configuration.service.UsersService;
import com.example.ecommerce.usersrepo.UsersRepository;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private UsersService userRepo;
    
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users userRes = null;
		userRes = userRepo.findByEmail(email);
        if(userRes==null)
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        return new org.springframework.security.core.userdetails.User(
                email,
                userRes.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
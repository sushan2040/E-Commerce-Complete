package com.example.ecommerce.configuration.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.ecommerce.configuration.masters.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuditorAwareImpl implements AuditorAware<Integer> {
	
	@Autowired
	JwtService jwtService;
	
    @Override
    public Optional<Integer> getCurrentAuditor() {
        // Fetch the current logged-in user from the security context, for example
        // This assumes you have security configured (e.g., Spring Security)
        // Extract the Authorization header
    	// Get the current HTTP request from the context
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Example: Get the "Authorization" header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return Optional.ofNullable(null); // No token found
        }

        String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
        try {
            // Extract claims (e.g., expiration, subject) from the token
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);

            // Deserialize user info (if applicable)
            Users parsedUser = new ObjectMapper().readValue(user, Users.class);
            
            
        return Optional.of(parsedUser.getUserId()); // Replace with actual logic
        }catch(Exception e) {
        	e.printStackTrace();
        	return Optional.of(null);
        }
    }
}
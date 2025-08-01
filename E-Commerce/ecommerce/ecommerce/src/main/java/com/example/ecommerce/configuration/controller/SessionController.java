package com.example.ecommerce.configuration.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.masters.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api-data/session-check", produces = "application/json")
public class SessionController {
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/expired-or-not")
    public @ResponseBody ResponseEntity<Map<String, String>> isSessionExpired(
            HttpServletRequest request,
            @RequestHeader HttpHeaders headers) {

        Map<String, String> response = new HashMap<>();

        try {
            // Extract the Authorization header
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("status", "null");
                return ResponseEntity.ok(response);
            }

            String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix

            // Extract claims
            String userJson = jwtService.extractClaim(jwtToken, Claims::getSubject);
            Users parsedUser = new ObjectMapper().readValue(userJson, Users.class);

            // Validate token expiration
            boolean isValid = jwtService.isTokenValid(jwtToken, parsedUser);
            
            response.put("status", isValid ? "Not null" : "null");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "null"); // Return null in case of error
            return ResponseEntity.ok(response);
        }
    }
}

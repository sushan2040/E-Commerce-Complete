package com.example.ecommerce.configuration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Apply to all endpoints
//                .allowedOrigins("http://localhost:3000") // React front-end or allowed domains
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
//                .allowedHeaders("*"); // Allow all headers
//                //.allowCredentials(true) // Allow credentials (cookies, etc.)
//                //.maxAge(3600); // Cache the preflight request for 1 hour
//    }
}
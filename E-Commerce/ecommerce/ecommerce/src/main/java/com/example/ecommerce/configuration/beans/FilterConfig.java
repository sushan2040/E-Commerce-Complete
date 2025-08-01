package com.example.ecommerce.configuration.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class FilterConfig /* implements WebMvcConfigurer */ {

//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilter() {
//        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
//        UrlBasedCorsConfigurationSource config=new UrlBasedCorsConfigurationSource();
//        CorsConfiguration configuration=new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("GET","POST","DELETE","OPTIONS","OPTION"));
//        configuration.setAllowedHeaders(List.of("*"));
//        // Allow credentials if needed
//       // configuration.setAllowCredentials(true);
//        Map<String,CorsConfiguration> map=new HashMap<String, CorsConfiguration>();
//        map.put("/**", configuration);
//        config.setCorsConfigurations(map);
//        registrationBean.setFilter(new CorsFilter(config));
//        registrationBean.addUrlPatterns("/api-data/**"); // Apply filter to all endpoints
//        return registrationBean;
//    }
}
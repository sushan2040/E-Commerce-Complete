package com.example.ecommerce.configuration.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping(value = "/react")
public class ReactController {
	@GetMapping(value = "/index")
    public ResponseEntity<Resource> redirect() {
        try {
            // Using classpath to locate the file inside the JAR
            Resource resource = new ClassPathResource("static/index.html");
            return ResponseEntity.ok()
                                 .contentType(MediaType.TEXT_HTML)
                                 .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
	 @Value("${custom.api.url}")
	    private String apiUrl;

	    @GetMapping
	    public Map<String, String> getConfig() {
	        return Map.of("apiUrl", apiUrl);
	    }
}
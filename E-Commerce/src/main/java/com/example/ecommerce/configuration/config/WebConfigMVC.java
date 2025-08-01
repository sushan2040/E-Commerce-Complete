package com.example.ecommerce.configuration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigMVC implements WebMvcConfigurer  {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // Point to the custom static directory inside target/classes or packaged JAR
	    registry.addResourceHandler("/static/**")  // This maps the URL pattern to match static files
	            .addResourceLocations("classpath:/static/static/")  // Points to /static folder in classpath
	            .setCachePeriod(0);  // Optional: Disable caching for development (set to a higher value for production)
	}
}

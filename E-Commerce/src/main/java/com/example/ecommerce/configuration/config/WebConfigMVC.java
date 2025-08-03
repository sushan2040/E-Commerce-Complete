package com.example.ecommerce.configuration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigMVC implements WebMvcConfigurer  {
	
	@Autowired
	Environment environment;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // Point to the custom static directory inside target/classes or packaged JAR
	    registry.addResourceHandler("/static/**")  // This maps the URL pattern to match static files
	            .addResourceLocations("classpath:/static/static/")  // Points to /static folder in classpath
	            .setCachePeriod(0);  // Optional: Disable caching for development (set to a higher value for production)
	    
	    registry.addResourceHandler("/product/images/**")
	    .addResourceLocations(environment.getProperty("product.images.path"))
	    .setCachePeriod(0);
	}
}

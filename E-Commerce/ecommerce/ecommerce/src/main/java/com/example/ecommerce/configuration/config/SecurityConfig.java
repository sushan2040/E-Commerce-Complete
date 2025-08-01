package com.example.ecommerce.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private final AuthenticationProvider authenticationProvider;
	    private final JwtAuthenticationFilter jwtAuthenticationFilter;

	    public SecurityConfig(
	        JwtAuthenticationFilter jwtAuthenticationFilter,
	        AuthenticationProvider authenticationProvider
	    ) {
	        this.authenticationProvider = authenticationProvider;
	        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    	http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    	http.cors().and().csrf().disable()
	        .authorizeHttpRequests()
	        .anyRequest().permitAll()
//	            .requestMatchers(HttpMethod.GET, "/api-data/**")  
//	            .authenticated()
//	            .requestMatchers(HttpMethod.POST, "/api-data/**")  
//	            .authenticated()
//	            .requestMatchers(HttpMethod.GET, "/swagger-ui/**")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.POST, "/swagger-ui/**")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.GET, "/v3/api-docs")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.POST, "/v3/api-docs")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.GET, "/swagger.html")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.POST, "/api/auth/login")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.POST, "/api/auth/register")  
//	            .permitAll()
//	            .requestMatchers(HttpMethod.POST, "/api/auth/get-user-access")  
//	            .authenticated()
//	            .requestMatchers(HttpMethod.POST, "/login")  // Allow POST requests to login
//	            .permitAll()  // No authentication required
//	            .anyRequest().authenticated() 
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and()
            .authenticationProvider(authenticationProvider);
            //.addFilterBefore(jwtAuthenticationFilter, AuthenticationProvider.class);

    return http.build();
}

}

package com.example.ecommerce.configuration.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.ecommerce.configuration.masters.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


	private final HandlerExceptionResolver handlerExceptionResolver;
	private JwtService jwtService;
	
	private final UserDetailsService userDetailsService;
	
	public JwtAuthenticationFilter(JwtService jwtService,
			UserDetailsService userDetailsService,
			HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtService=jwtService;
		this.userDetailsService=userDetailsService;
		this.handlerExceptionResolver=handlerExceptionResolver;
	}
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader=request.getHeader("Authorization");
		if(authHeader==null || !authHeader.startsWith("Bearer ") || authHeader.substring(7).equals("null")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			final String jwt=authHeader.substring(7);
			final String userEmail=jwtService.extractUsername(jwt);
			Users userFromJwt=new ObjectMapper().readValue(userEmail,Users.class);
			Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
			if(userFromJwt!=null && authentication==null) {
				UserDetails userDetails=this.userDetailsService.loadUserByUsername(userFromJwt.getUsername());
				if(jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),
							userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}
			filterChain.doFilter(request, response);
		}catch(Exception exception) {
			logger.error("JWT Authentication failed", exception);
		    handlerExceptionResolver.resolveException(request, response, null, exception);
			handlerExceptionResolver.resolveException(request, response, null,exception);
		}
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if(request.getServletPath().endsWith("/login") || request.getServletPath().endsWith("/register") || 
				request.getServletPath().startsWith("/country") || request.getServletPath().startsWith("/ecommerce") || request.getServletPath().startsWith("/home")
				||request.getServletPath().endsWith("/fetch-all-countries")) {
			return true;
		}else {
			return false;
		}
	}
	
}

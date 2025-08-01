package com.example.ecommerce.configuration.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class JWTFilter {
//
//    @Autowired private MyUserDetailsService userDetailsService;
//    @Autowired private JWTUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
//            String jwt = authHeader.substring(7);
//            if(jwt == null || jwt.isBlank()){
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
//            }else {
//                try{
//                    String email = jwtUtil.validateTokenAndRetrieveSubject(jwt);
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
//                    if(SecurityContextHolder.getContext().getAuthentication() == null){
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                }catch(JWTVerificationException exc){
//                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//   @Override 
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//		return request.getServletPath().endsWith("/login");
//	}
}
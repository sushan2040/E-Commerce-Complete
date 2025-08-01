package com.example.ecommerce.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.springframework.http.HttpHeaders;

import com.example.ecommerce.configuration.config.JwtService;
import com.example.ecommerce.configuration.masters.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
	
	private static JwtService jwtService;

    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String getMacAddress(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
            
            if (networkInterface == null) {
                return null;
            }
            
            byte[] mac = networkInterface.getHardwareAddress();
            if (mac == null) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X", mac[i]));
                if (i != mac.length - 1) {
                    sb.append(":");
                }
            }
            return sb.toString();

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Users getUserFromHeaders(HttpHeaders headers) {
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null; // No token found
        }
        String jwtToken = authHeader.substring(7); // Remove "Bearer " prefix
            // Extract claims (e.g., expiration, subject) from the token
            String user = jwtService.extractClaim(jwtToken, Claims::getSubject);
            // Deserialize user info (if applicable)
            Users parsedUser=null;
            try {
				parsedUser = new ObjectMapper().readValue(user, Users.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
            return parsedUser;
    }
}

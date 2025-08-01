package com.example.ecommerce.configuration.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.ecommerce.configuration.masters.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {
   // @Value("${security.jwt.secret-key}")
    private String secretKey;

   // @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
    
    @Autowired
    Environment environment;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, (Long.parseLong(environment.getProperty("security.jwt.expiration-time"))*60*1000));
    }

    public long getExpirationTime() {
        return (Long.parseLong(environment.getProperty("security.jwt.expiration-time"))*60*1000);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails2,
            long expiration
    ) {
    	String userdetails="";
		try {
			userdetails = new ObjectMapper().writeValueAsString(userDetails2);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userdetails)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        ObjectMapper mapper=new ObjectMapper();
        try {
			Users users=mapper.readValue(username,Users.class);
			  return (users.getUsername().equals(userDetails.getUsername())) && !isTokenExpired(token);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
      
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(environment.getProperty("security.jwt.secret-key"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
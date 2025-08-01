package com.example.ecommerce.configuration.config;

//@Component
public class JWTUtil {
//	
//	@Autowired
//	Environment environment;
//	
//	public String generateToken(String email) throws JWTCreationException,IllegalArgumentException{
//		Calendar cal=Calendar.getInstance();
//		cal.add(Calendar.MINUTE,15);
//		return JWT.create()
//				.withClaim("email",email)
//				.withIssuedAt(new Date())
//				.withIssuer("ecommerce")
//				.withExpiresAt(cal.getTime())
//				.sign(Algorithm.HMAC256(environment.getProperty("jwt.secret")));
//				
//	}
//	public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
//        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(environment.getProperty("jwt.secret")))
//                .withIssuer("ecommerce")
//                .build();
//        DecodedJWT jwt = verifier.verify(token);
//        return jwt.getClaim("email").asString();
//    }
//	public boolean isTokenExpired(String jwtToken, String secretKey) {
//	    try {
//	        // Parse the JWT to get the Claims (payload)
//	        Claims claims = Jwts.parser()
//	                            .setSigningKey(secretKey.getBytes())  // Your secret key to validate the token signature
//	                            .build()
//	                            .parseClaimsJws(jwtToken)
//	                            .getBody();
//	        
//	        // Extract the expiration date (exp claim)
//	        Date expiration = claims.getExpiration();
//	        
//	        // Check if the token is expired
//	        return expiration.before(new Date());  // If the expiration date is before the current time, the token is expired
//	    } catch (io.jsonwebtoken.security.SignatureException e) {
//	        // Handle invalid token signature
//	        throw new RuntimeException("Invalid JWT signature", e);
//	    } catch (Exception e) {
//	        // Handle other exceptions (e.g., if the token is malformed or expired)
//	        throw new RuntimeException("Error decoding the JWT", e);
//	    }
//	}
}

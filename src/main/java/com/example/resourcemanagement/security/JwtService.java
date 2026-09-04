package com.example.resourcemanagement.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component	
public class JwtService {

	private String secret;
	private final long expiration=1000*60*60;
	
	public String generateToken(String email, String role) {
		SecretKey key=Keys.hmacShaKeyFor(
				secret.getBytes(StandardCharsets.UTF_8));
		
		return Jwts.builder()
				.subject(email)
				.claim("role",role)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+expiration))
				.signWith(key)
				.compact();
	}
	
	public String extractEmail(String token) {
		SecretKey key = Keys.hmacShaKeyFor(
		        secret.getBytes(StandardCharsets.UTF_8)
		);
		
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
}

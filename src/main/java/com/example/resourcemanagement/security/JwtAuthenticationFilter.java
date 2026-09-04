package com.example.resourcemanagement.security;


import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.resourcemanagement.entity.User;
import com.example.resourcemanagement.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String authHeader=request.getHeader("Authorization");
		System.out.println("Authorization Header"+authHeader);
		
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token=authHeader.substring(7);
		
		String email=jwtService.extractEmail(token);
		
		System.out.println("Email from JWT: " + email);
		
		User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
		
		System.out.println("User found: " + user.getEmail());
		
		System.out.println("JWT token "+token );
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				user.getEmail(),
				null,
				List.of(()->"Role_"+user.getRole().name()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		filterChain.doFilter(request, response);
	}

}

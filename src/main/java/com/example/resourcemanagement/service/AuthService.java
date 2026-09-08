package com.example.resourcemanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.resourcemanagement.dto.request.LoginRequest;
import com.example.resourcemanagement.dto.request.RegisterRequest;
import com.example.resourcemanagement.dto.response.LoginResponse;
import com.example.resourcemanagement.entity.User;
import com.example.resourcemanagement.enums.Role;
import com.example.resourcemanagement.repository.UserRepository;
import com.example.resourcemanagement.security.JwtService;

import jakarta.validation.Valid;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	public AuthService(UserRepository userRepository , PasswordEncoder passwordEncoder , JwtService jwtService) {
		this.passwordEncoder=passwordEncoder;
		this.userRepository=userRepository;
		this.jwtService=jwtService;
	}
	
	public LoginResponse login(LoginRequest request) {
		User user=userRepository.findByEmail(request.getEmail())
				.orElseThrow(()->new RuntimeException("Invalid mail or password"));
		
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("invalid mail or password");
		}
		String token=jwtService.generateToken(user.getEmail(),user.getRole().name());
		
		return new LoginResponse(token);
	}

	public void register(@Valid RegisterRequest request) {
		// TODO Auto-generated method stub
		if(userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("Email already registered");
		}
		User user=new User();
		user.setUserName(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);
		
	}
}

package com.example.resourcemanagement.dto.response;

public class LoginResponse {

	private String token;

	public LoginResponse(String token) {
		super();
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
}

package com.cogent.fooddeliveryapp.payload.response;

import java.util.List;

import lombok.Data;
@Data
public class JwtResponse {

	private String token;
	
	private String type = "bearer";
	private Long id;
	private String userName;
	private String email;
	private List<String> roles;
	
	
	public JwtResponse(String accessToken, Long id, String userName, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.roles = roles;
	}	
	
}

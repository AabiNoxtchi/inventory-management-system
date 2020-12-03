package com.inventory.inventory.auth.Models;

public class LoginResponse {
	
	private String token;
	private Long id;
	private String userName;
	private String role;
	
	
	public LoginResponse(String token, Long id, String userName, String role) {
		this.id=id;
		this.token = token;
		this.userName = userName;
		this.role = role;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	public LoginResponse() {
		
	}

}

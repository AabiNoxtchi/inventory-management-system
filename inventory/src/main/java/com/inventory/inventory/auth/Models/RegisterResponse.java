package com.inventory.inventory.auth.Models;

public class RegisterResponse {
	
	private String message;
	
	private boolean refreshToken;
	private String jwtToken; 
	
	public RegisterResponse(String message, boolean refreshToken, String jwtToken) {
		super();
		this.message = message;
		this.refreshToken = refreshToken;
		this.jwtToken = jwtToken;
	}
	
	public RegisterResponse(String message,String jwt) {
	    this.message = message;
	    this.jwtToken=jwt;
	  }
	
	public RegisterResponse(String message) {
	    this.message = message;	   
	  }

	public boolean isRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(boolean refreshToken) {
		this.refreshToken = refreshToken;
	}
	

	public String getJwtToken() {
		return jwtToken;
	}	
	
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

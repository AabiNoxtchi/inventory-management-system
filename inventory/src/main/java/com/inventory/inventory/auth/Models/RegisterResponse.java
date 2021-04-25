package com.inventory.inventory.auth.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RegisterResponse {
	
	private String message;	
	private boolean refreshToken;
	private String jwtToken;
	private String userName;
	
	public RegisterResponse(String message) {
	    this.message = message;	   
    }
	
	public RegisterResponse(String message, boolean refreshToken, String jwtToken , String userName) {
		super();
		this.message = message;
		this.refreshToken = refreshToken;
		this.jwtToken = jwtToken;
		this.userName = userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}

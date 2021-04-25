package com.inventory.inventory.ViewModels.PendingUser;

public class PendingUserDAO {
	
	private Long id;
	private String username;
	private String email;
	private String newCity;
    private Long countryId;    
    
	public PendingUserDAO(Long id, String username, String email, String newCity, Long countryId) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.newCity = newCity;
		this.countryId = countryId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNewCity() {
		return newCity;
	}
	public void setNewCity(String newCity) {
		this.newCity = newCity;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

}

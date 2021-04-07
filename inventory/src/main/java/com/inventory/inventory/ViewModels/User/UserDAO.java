package com.inventory.inventory.ViewModels.User;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;

public class UserDAO {
	
	private Long id;	
	private String firstName; 
    private String lastName;
	private String userName;
	private String email;
	
	private boolean deleted;

	private String countryName;
	private String cityName;
	
	
	
	public UserDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserDAO(Long id, String firstName, String lastName, String userName, String email, String countryName,
			String cityName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		this.countryName = countryName;
		this.cityName = cityName;
	}

	public UserDAO(Long id, String firstName, String lastName, String userName, String email, LocalDate deleted) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.email = email;
		
		this.deleted = deleted != null;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	

}

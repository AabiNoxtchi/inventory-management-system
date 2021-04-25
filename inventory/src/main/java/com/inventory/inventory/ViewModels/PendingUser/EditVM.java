package com.inventory.inventory.ViewModels.PendingUser;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.auth.Models.RegisterRequest;

@JsonInclude(Include.NON_NULL)
public class EditVM extends BaseEditVM<RegisterRequest>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull
	private Long id;
	private String username;
	private String email;
	
	@NotNull
	private Long cityId;
    private Long countryId;

	@Override
	public void populateModel(RegisterRequest item) {		
	}

	@Override
	public void populateEntity(RegisterRequest item) {
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
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
}

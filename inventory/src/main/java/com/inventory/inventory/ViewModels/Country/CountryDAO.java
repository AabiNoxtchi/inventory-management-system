package com.inventory.inventory.ViewModels.Country;

import java.util.ArrayList;
import java.util.List;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;

public class CountryDAO {
	
	private Long id;
	private String name;	
	private String currency;
	
	private String code;
	
	private String phoneCode;
	
	private List<City> cities = new ArrayList<>();
	
	
	public CountryDAO() {
		super();
	}

	public CountryDAO(Country c, List<City> cities) {
		this.id = c.getId();
		this.name = c.getName();
		this.currency = c.getCurrency();
		this.cities = cities;
		this.code = c.getCode();
		this.phoneCode = c.getPhoneCode();
	}
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

}

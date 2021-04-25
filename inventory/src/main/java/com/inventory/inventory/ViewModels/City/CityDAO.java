package com.inventory.inventory.ViewModels.City;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;

public class CityDAO {
	
	private Long id;
	private String name;
	private String timeZone;	
	private Country country;
	
	
	public CityDAO() {
		super();
	}
	
	public CityDAO(City city, Country country) {
		super();
		this.id = city.getId();
		this.name = city.getName();
		this.timeZone = city.getTimeZone();
		this.country = country;
	}
	
	public CityDAO(Long id, String name, String timeZone, Country country) {
		super();
		this.id = id;
		this.name = name;
		this.timeZone = timeZone;
		this.country = country;
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
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}

}

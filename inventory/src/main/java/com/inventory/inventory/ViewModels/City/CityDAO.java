package com.inventory.inventory.ViewModels.City;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;

public class CityDAO {
	
	private Long id;
	private String name;
	private String timeZone;	
	private Country country;
	
	
	public CityDAO() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
	
//	private Long id;
//	private String name;	
//	private String currency;
//	
//	private List<City> cities = new ArrayList<>();
//	
//	
//	public CountryDAO() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	
//	public CountryDAO(Long id, String name, String currency, List<City> cities) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.currency = currency;
//		this.cities = cities;
//	}
//
//	public CountryDAO(Country c, List<City> cities) {
//		this.id = c.getId();
//		this.name = c.getName();
//		this.currency = c.getCurrency();
//		this.cities = cities;
//	}
//
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getCurrency() {
//		return currency;
//	}
//	public void setCurrency(String currency) {
//		this.currency = currency;
//	}
//	public List<City> getCities() {
//		return cities;
//	}
//	public void setCities(List<City> cities) {
//		this.cities = cities;
//	}
//	
//	

}

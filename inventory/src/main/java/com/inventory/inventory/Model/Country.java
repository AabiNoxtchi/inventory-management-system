package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "country",
		uniqueConstraints = { 
		@UniqueConstraint(name = "country-name", columnNames = "name")
		})
public class Country extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	
	private String name;
	
	private String currency;
	
	private String phoneCode;	
	
	@OneToMany(mappedBy="country", cascade = CascadeType.ALL)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<City> cities = new ArrayList<>();

	public Country() {
		super();
	}
	
	public Country(Long countryId) {
		setId(countryId);
	}

	public Country(String name, String currency) {
		super();
		this.name = name;
		this.currency = currency;
	}
	
	public Country(Long id, String code , String name) {
		setId(id);
		this.name = name;
		this.code = code;
	}
	
	public void addCity(City city) {
        this.cities.add(city);
        city.setCountry(this);
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
	
	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	 

}



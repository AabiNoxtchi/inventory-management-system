package com.inventory.inventory.Model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "city",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name")
		})
public class City extends BaseEntity implements Serializable{
	
	private String name;
	private String timeZone;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private Country country;
	
	@Formula("(select country_id)")
	private Long countryId;
	
	

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public City(Long id) {		
		this.setId(id);	
	}
	
	

	public City(String name, String timeZone) {
		super();
		this.name = name;
		this.timeZone = timeZone;
		//this.country = country;
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
	
	

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", timeZone=" + timeZone + ", country=" + country + "]";
	}
	
	

}

package com.inventory.inventory.Model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "city",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = {"name", "country_id"}, name="city")
		})
public class City extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Size(max = 100)
	private String name;
	@Size(max = 100)
	private String timeZone;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private Country country;
	
	@Formula("(select country_id)")
	private Long countryId;

	public City() {
		super();
	}
	
	public City(Long id) {		
		this.setId(id);	
	}

	public City(String name, String timeZone) {
		super();
		this.name = name;
		this.timeZone = timeZone;
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

}

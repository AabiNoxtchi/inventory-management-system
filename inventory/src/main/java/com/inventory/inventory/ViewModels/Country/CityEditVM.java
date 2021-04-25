package com.inventory.inventory.ViewModels.Country;

import java.util.List;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class CityEditVM {
	
	private Long id;
	private String name;
	
	@DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
	private List<SelectItem> countries;
	private Long countryId;
	
	private List<SelectItem> zones;
	private String timeZone;
	
	public void populateModel(City item) {
		setId(item.getId());
		name = item.getName();
		timeZone = item.getTimeZone();
		countryId = item.getCountryId();		
	}
	
	public void populateEntity(City item) {
	
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
	public List<SelectItem> getCountries() {
		return countries;
	}
	public void setCountries(List<SelectItem> countries) {
		this.countries = countries;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public List<SelectItem> getZones() {
		return zones;
	}
	public void setZones(List<SelectItem> zones) {
		this.zones = zones;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}

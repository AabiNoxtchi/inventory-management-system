package com.inventory.inventory.ViewModels.City;

import java.util.List;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<City>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private Long ;
	private String name;
	
	//existing countries
	@DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
	private List<SelectItem> countries;
	private Long countryId;
	
	
	
	//all zones from references
	private List<SelectItem> zones;
	private String timeZone;
	
	
	/*************************** new Country *****************************/
	
	
	
	/*************************** new Country *****************************/

	@Override
	public void populateModel(City item) {
		setId(item.getId());
		name = item.getName();
		timeZone = item.getTimeZone();
		countryId = item.getCountryId();
		
	}

	@Override
	public void populateEntity(City item) {
		item.setId(getId());
		item.setName(name);
		item.setTimeZone(timeZone);
		item.setCountry(new Country(countryId));
		
	}

//	public Long getCityId() {
//		return cityId;
//	}
//
//	public void setCityId(Long cityId) {
//		this.cityId = cityId;
//	}

	

	

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
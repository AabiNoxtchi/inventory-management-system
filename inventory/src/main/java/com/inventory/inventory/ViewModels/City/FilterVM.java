package com.inventory.inventory.ViewModels.City;

import java.util.HashMap;
import java.util.List;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM{	
	
	@DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
	private List<SelectItem> countries;//existing in data base
	private Long countryId;
	
	@DropDownAnnotation(target="cityId",value="city.id", name="city.name",title="select city", filterBy="countryId")
	private List<SelectItem> cities;
	private Long cityId;
	
	private List<SelectItem> zones;//existing in data base
	private String timeZone;
	
	private List<SelectItem> currencies;//existing in data base
	private String currency;	

	@Override
	public Predicate getPredicate() {

		System.out.println("city filter city id = "+cityId);
		QCity city = QCity.city;

		Predicate p = 
				(countryId == null ? Expressions.asBoolean(true).isTrue() 
						:city.country.id.eq(countryId) )
						.and(cityId == null ? Expressions.asBoolean(true).isTrue() 
						: city.id.eq(cityId))
						.and(timeZone == null ? Expressions.asBoolean(true).isTrue()
						:city.timeZone.eq(timeZone)) 
						.and(currency == null ? Expressions.asBoolean(true).isTrue() 
						: city.country.currency.eq(currency))							
					  ;	
				System.out.println("predicate p = "+p);
							
		return p;
	}

	@Override
	public void setDropDownFilters() {
		Predicate p = Expressions.asBoolean(true).isTrue();
		dropDownFilters = new HashMap<String, Predicate>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("countries", p);
				put("cities", p);				
			}};
		
	}

	@Override
	public Boolean getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAll(Boolean all) {
		// TODO Auto-generated method stub		
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

	public List<SelectItem> getCities() {
		return cities;
	}

	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
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

	public List<SelectItem> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<SelectItem> currencies) {
		this.currencies = currencies;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	

	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {
		return QCity.city.id.eq(id);
	}

	@Override
	public Predicate getListAuthorizationPredicate(List<Long> ids, ERole eRole, Long userId) {
		// TODO Auto-generated method stub
		return QCity.city.id.in(ids);
	}
	
	

}
package com.inventory.inventory.ViewModels.Country;

import java.util.HashMap;
import java.util.List;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

//import static com.querydsl.sql.SQLExpressions.select;
//import static com.inventory.inventory.ViewModels.Delivery.SQLExpressions.select;

public class FilterVM extends BaseFilterVM{
	
	
	@DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
	private List<SelectItem> countries;//existing in data base
	private Long countryId;
	
	@DropDownAnnotation(target="cityId",value="city.id", name="city.name",title="select city", filterBy="countryId")
	private List<SelectItem> cities;
	private Long cityId;
	
	@DropDownAnnotation(target="timeZone",value="city.timeZone", name="city.timeZone",title="select time zone")
	private List<SelectItem> zones;//existing in data base
	private String timeZone;
	
	@DropDownAnnotation(target="currency",value="country.currency", name="country.currency",title="select currency")
	private List<SelectItem> currencies;//existing in data base
	private String currency;
	
		
		//private Country country;

	@Override
	public Predicate getPredicate() {

		System.out.println("country filter city id = "+cityId);
		QCity city = QCity.city;
		QCountry country = QCountry.country;

		Predicate p = 
				(countryId == null ? Expressions.asBoolean(true).isTrue() 
						:country.id.eq(countryId) )
						.and(cityId == null ? Expressions.asBoolean(true).isTrue() 
						: country.id.eq(JPAExpressions.selectFrom(city).where(city.id.eq(cityId)).select(city.countryId)))
						.and(timeZone == null ? Expressions.asBoolean(true).isTrue()
						:country.id.in(JPAExpressions.selectFrom(city).where(city.timeZone.eq(timeZone)).select(city.countryId))) 
						.and(currency == null ? Expressions.asBoolean(true).isTrue() 
						: country.currency.eq(currency))							
					  ;	
				System.out.println("predicate p = "+p);
							
		return p;
	}

	@Override
	public void setDropDownFilters() {
		Predicate p = Expressions.asBoolean(true).isTrue();
		dropDownFilters = new HashMap<String, Predicate>() {
			{
				put("countries", p);
				put("cities", p);	
				put("zones", QCity.city.timeZone.isNotNull().and(p));
				put("currencies", QCountry.country.currency.isNotNull().and(p));
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
		return QCountry.country.id.eq(id);
	}
	
	

}
package com.inventory.inventory.ViewModels.City;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

//import static com.querydsl.sql.SQLExpressions.select;
//import static com.inventory.inventory.ViewModels.Delivery.SQLExpressions.select;

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
	
		
		//private Country country;

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
	
	

}
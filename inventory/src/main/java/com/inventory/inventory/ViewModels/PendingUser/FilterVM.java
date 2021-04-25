package com.inventory.inventory.ViewModels.PendingUser;

import java.util.HashMap;
import java.util.List;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.QRegisterRequest;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

public class FilterVM extends BaseFilterVM {
	
	private String newCity;
	
	@DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
	private List<SelectItem> countries;
    private Long countryId;
    
  @DropDownAnnotation(target="cityId",value="city.id", name="city.name",title="select city", filterBy="countryId")
	private List<SelectItem> cities;

	@Override
	public Predicate getPredicate() {
		return countryId == null ? Expressions.asBoolean(true).isTrue():
			QRegisterRequest.registerRequest.countryId.eq(countryId)
			.and(newCity == null ? Expressions.asBoolean(true).isTrue() :
				QRegisterRequest.registerRequest.newCity.containsIgnoreCase(newCity));		
	}

	@Override
	public void setDropDownFilters() {
		Predicate p = Expressions.asBoolean(true).isTrue();		
		Predicate pCountry = 
				 QCountry.country.id.in(JPAExpressions.selectFrom(QRegisterRequest.registerRequest)
						 .distinct().select(QRegisterRequest.registerRequest.countryId));
				 
		dropDownFilters = new HashMap<String, Predicate>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			{
				put("countries", pCountry);
				put("cities", p);	
			}};		
	}

	@Override
	public Boolean getAll() {
		return null;
	}
	@Override
	public void setAll(Boolean all) {
	}
	public String getNewCity() {
		return newCity;
	}
	public void setNewCity(String newCity) {
		this.newCity = newCity;
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
	
	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {		
		return QRegisterRequest.registerRequest.id.eq(id);
	}

	@Override
	public Predicate getListAuthorizationPredicate(List<Long> ids, ERole eRole, Long userId) {
		// TODO Auto-generated method stub
		return QRegisterRequest.registerRequest.id.in(ids);
	}
	
	

}
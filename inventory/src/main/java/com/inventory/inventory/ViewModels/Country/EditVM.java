package com.inventory.inventory.ViewModels.Country;

import java.util.List;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<Country>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@DropDownAnnotation(target="name",value="country.id", name="country.name",title="select country")
	private List<SelectItem> allCountries;
	private String name;
	
	private String code;
	
	@DropDownAnnotation(target="phoneCode",value="country.code", name="country.phoneCode",title="select phone code")
	private List<SelectItem> allPhoneCodes;
	private String phoneCode;
		
	//all currencies from references
	private List<SelectItem> currencies;
	private String currency;
	
	//private List<City> cities;
	private List<CityEditVM> cityVMs;
	
	
	@Override
	public void populateModel(Country item) {
		setId(item.getId());
		name=item.getName();
		currency=item.getCurrency();
		
	}
	@Override
	public void populateEntity(Country item) {
	
		item.setId(getId());
		item.setName(name);
		item.setCurrency(currency);		
	}
	
	
	public List<SelectItem> getAllCountries() {
		return allCountries;
	}
	public void setAllCountries(List<SelectItem> allCountries) {
		this.allCountries = allCountries;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public List<CityEditVM> getCityVMs() {
		return cityVMs;
	}
	public void setCityVMs(List<CityEditVM> cityVMs) {
		this.cityVMs = cityVMs;
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
	public List<SelectItem> getAllPhoneCodes() {
		return allPhoneCodes;
	}
	public void setAllPhoneCodes(List<SelectItem> allPhoneCodes) {
		this.allPhoneCodes = allPhoneCodes;
	}
	
	
	
	
	
	

}
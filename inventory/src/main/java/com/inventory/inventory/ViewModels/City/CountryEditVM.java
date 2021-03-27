package com.inventory.inventory.ViewModels.City;

import java.util.List;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class CountryEditVM {
	
	private Long id;
	
	//all countries from references
		@DropDownAnnotation(target="name",value="country.id", name="country.name",title="select country")
		private List<SelectItem> allCountries;
		private String name;
			
		//all currencies from references
		private List<SelectItem> currencies;
		private String currency;
		
		
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
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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

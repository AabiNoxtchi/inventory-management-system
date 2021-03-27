package com.inventory.inventory.ViewModels.City;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.money.CurrencyUnit;

import com.inventory.inventory.ViewModels.Shared.SelectItem;



public class References {
	
	 private List<CurrencyUnit> currencyUnits ;
	
	 private List<String> zids ;
	 
	 private List<String> countries ;
	 private List<SelectItem> countrySelects ;

	 public List<String> getCountries() {
		 if(countries == null) {
			 countries = new ArrayList<>();
		 String[] isoCountries = Locale.getISOCountries();
	     for (String country : isoCountries) {
	         Locale locale = new Locale("en", country);        
	         String name = locale.getDisplayCountry();

	         if (!"".equals(name)) {
	             countries.add( name);
	         }
	     }

	     Collections.sort(countries);
		 }
	     return countries;
	}
	 
	 public List<SelectItem> getCountrySelects() {
		 if(countrySelects == null) {
			 countrySelects = new ArrayList<>();
		 String[] isoCountries = Locale.getISOCountries();
	     for (String country : isoCountries) {
	         Locale locale = new Locale("en", country);        
	         String name = locale.getDisplayCountry();

	         if (!"".equals(name)) {
	             countrySelects.add(new SelectItem(name,name));
	         }
	     }

	    // Collections.sort(countrySelects.);
		 }
	     return countrySelects;
	}
	 
	 public List<SelectItem> getCountrySelectsMinus(List<SelectItem> countries) {
		
			List<SelectItem> countrySelects = new ArrayList<>();
		 String[] isoCountries = Locale.getISOCountries();
	     for (String country : isoCountries) {
	         Locale locale = new Locale("en", country);        
	         String name = locale.getDisplayCountry();
	        
	         if (!"".equals(name) && !countries.stream().anyMatch(x->x.getName().equals(name))) {
	             countrySelects.add(new SelectItem(name,name));
	         }
	     }

	    // Collections.sort(countries);
		 
	     return countrySelects;
	}

	public List<CurrencyUnit> getCurrencyUnits() {
		return CurrencyUnit.registeredCurrencies();
	}
	public List<SelectItem> getCurrencySelects() {
		return CurrencyUnit.registeredCurrencies().stream().map(i-> new SelectItem(i.toString(), i.toString())).collect(Collectors.toList());
	}

	public List<String> getZids() {
		
		Set <String> zids = ZoneId.getAvailableZoneIds();
		List<String> zidsList = zids.stream().collect(Collectors.toList());
		Collections.sort(zidsList, (o1, o2) -> o1.compareTo(o2));
		return zidsList;
	}
	public List<SelectItem> getZidSelects() {
		
		Set <String> zids = ZoneId.getAvailableZoneIds();
		List<SelectItem> zidsList = zids.stream().map(i-> new SelectItem(i,i)).collect(Collectors.toList());
		Collections.sort(zidsList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
		return zidsList;
	}

	
	

}

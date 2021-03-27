package com.inventory.inventory.ViewModels.City;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.money.CurrencyUnit;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.inventory.inventory.ViewModels.Shared.SelectItem;



public class References {
	
	 private List<CurrencyUnit> currencyUnits ;
	 private List<SelectItem> currencySelects;
	
	 private List<String> zids ;
	 private List<SelectItem> zidSelects ;
	 
	 private List<String> countries ;
	 private List<SelectItem> countrySelects ;
	 
	 private List<SelectItem> phoneCodes;

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
			 countrySelects = //getCountries().stream().map(c->new SelectItem(c, c)).collect(Collectors.toList());}
					 new ArrayList<>();
		 String[] isoCountries = Locale.getISOCountries();
	     for (String country : isoCountries) {
	         Locale locale = new Locale("en", country);        
	         String name = locale.getDisplayCountry();

	         if (!"".equals(name)) {
	             countrySelects.add(new SelectItem(country,name));
	         }
	     }

	     //Collections.sort(countrySelects.);
	     Collections.sort(countrySelects, 
				    Comparator.comparing(SelectItem::getName));
		 }
	     return countrySelects;
	}
	 
	 public List<SelectItem> getCountrySelectsMinus(List<SelectItem> countries) {
		
		 List<SelectItem> countrySelectsMinus = new ArrayList<>();
		 String[] isoCountries = Locale.getISOCountries();
	     for (String country : isoCountries) {
	         Locale locale = new Locale("en", country);        
	         String name = locale.getDisplayCountry();
	        
	         if (!"".equals(name) && !countries.stream().anyMatch(x->x.getName().equals(name))) {
	             countrySelectsMinus.add(new SelectItem(country,name));
	         }
	         
	     }

	    // Collections.sort(countries);
	     Collections.sort(countrySelectsMinus, 
				    Comparator.comparing(SelectItem::getName));
		 
	     return countrySelectsMinus;
	}

	public List<CurrencyUnit> getCurrencyUnits() {
		if(currencyUnits==null)
			currencyUnits = CurrencyUnit.registeredCurrencies();
		return currencyUnits;
	}
	public List<SelectItem> getCurrencySelects() {
		if(currencySelects == null)
			currencySelects = 
					getCurrencyUnits().stream().map(i-> new SelectItem(i.toString(), i.toString())).collect(Collectors.toList());
		return currencySelects;
	}

	public List<String> getZids() {	
		if(zids == null) {			 
		zids = (ZoneId.getAvailableZoneIds()).stream().collect(Collectors.toList());
		Collections.sort(zids, (o1, o2) -> o1.compareTo(o2));
		}
		return zids;
	}
	
	public List<SelectItem> getZidSelects() {
		if(zidSelects == null)
			zidSelects = getZids().stream().map(i-> new SelectItem(i,i)).collect(Collectors.toList());
		//Set <String> zids = ZoneId.getAvailableZoneIds();
		//List<SelectItem> zidsList = zids.stream().map(i-> new SelectItem(i,i)).collect(Collectors.toList());
		//Collections.sort(zidsList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
		return zidSelects;
	}

	public List<SelectItem> getPhoneCodes() {
		if(phoneCodes == null) {			
			phoneCodes = new ArrayList<>();
			for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {				
	            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
	            phoneCodes.add(new SelectItem(cc, phoneCode+""));
	    		
			}		
		}
		return phoneCodes;
	}
	

	
}

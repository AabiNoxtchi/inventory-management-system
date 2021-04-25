package com.inventory.inventory.ViewModels.City;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.inventory.inventory.ViewModels.Shared.SelectItem;


public class References {	
	
	 private static List<String> zids ;
	 private static List<SelectItem> zidSelects ;	 
	
	 
	 public List<SelectItem> getCountrySelectsMinus(List<SelectItem> countries) {
		
		 List<SelectItem> countrySelectsMinus = new ArrayList<>();		
		 
		 String[] isoCountries = Locale.getISOCountries();
		 
		 if(countries.size() == isoCountries.length) return countrySelectsMinus;
	     for (String country : isoCountries) {
	         Locale locale = new Locale("en", country);        
	         String name = locale.getDisplayCountry();
	        
	         if (!"".equals(name) && !countries.stream().anyMatch(x->x.getName().equals(name))) {
	             countrySelectsMinus.add(new SelectItem(country,name));
	         }	         
	     }
	     Collections.sort(countrySelectsMinus, 
				    Comparator.comparing(SelectItem::getName));		 
	     return countrySelectsMinus;
	}

	 private synchronized void setZids() {		 
		 if(zids == null) {			 
				zids = (ZoneId.getAvailableZoneIds()).stream().collect(Collectors.toList());
				Collections.sort(zids, (o1, o2) -> o1.compareTo(o2));
				}				
		notify();;
	 }
	public List<String> getZids() {	
		if(zids == null) {			 
			setZids();
		}
		return zids;
	}
	
	private synchronized void setZidSelects() {	
			 if(zidSelects == null)
					zidSelects = getZids().stream().map(i-> new SelectItem(i,i)).collect(Collectors.toList());	
								
		notify();;
	 }
	public List<SelectItem> getZidSelects() {
		if(zidSelects == null)
			setZidSelects();		
		return zidSelects;
	}
	
	public List<SelectItem> getZidSelectsForRegion(String region){	
		List<SelectItem> items = new ArrayList<>();
		String[] timeZones = com.ibm.icu.util.TimeZone.getAvailableIDs(region);
		for(String s : timeZones) {			
			items.add(new SelectItem(s, s));
		 }		
		return items;		
	}
	
	public List<SelectItem> getPhoneCodes(List<SelectItem> allCountries) { 
		List<SelectItem> items = new ArrayList<>();
		
		for(SelectItem country : allCountries) {
			String cc = country.getValue();
	            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
	            String code = "+"+phoneCode;
	            items.add(new SelectItem(code, code));
		}	
			
		return items;
	}

	public List<SelectItem> getCurrencySelects(List<SelectItem> allCountries) { 
		
		List<SelectItem> items = new ArrayList<>();
		allCountries.stream().forEach( x -> {
			String currency = getCurrencyCode(x.getValue());
			String symbol = getCurrencySymbol(x.getValue());			
			symbol = symbol != null ? symbol : currency;
			items.add(new SelectItem(currency,symbol, x.getValue()));
		});
		
		return items;		
	}
	
	public static String getCurrencyCode(String countryCode) {
	    return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode(); 
	    
	}

	//to retrieve currency symbol 
	public static String getCurrencySymbol(String countryCode) {
	    return Currency.getInstance(new Locale("", countryCode)).getSymbol(); 
	}	

}

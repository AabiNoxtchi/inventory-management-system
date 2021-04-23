package com.inventory.inventory.ViewModels.City;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joda.money.CurrencyUnit;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.inventory.inventory.ViewModels.Shared.SelectItem;



public class References {
	
	 private static List<CurrencyUnit> currencyUnits ;
	 private static List<SelectItem> currencySelects;
	
	 private static List<String> zids ;
	 private static List<SelectItem> zidSelects ;
	 
	 private static List<String> countries ;
	 private static List<SelectItem> countrySelects ;
	 
	 private static List<SelectItem> phoneCodes;

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
		 if(countries.size() == isoCountries.length) return countrySelectsMinus;
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
	
	public List<SelectItem> getZidSelectsForRegion(String region){
		
		List<SelectItem> items = new ArrayList<>();
		String[] timeZones = com.ibm.icu.util.TimeZone.getAvailableIDs(region);
		for(String s : timeZones) {
			 //System.out.println("s = "+s);
			
			items.add(new SelectItem(s, s));
		 }
		
		return items;
		
	}

	public List<SelectItem> getPhoneCodes() {
		if(phoneCodes == null) {			
			phoneCodes = new ArrayList<>();
			for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {				
	            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
	            //phoneCodes.add(new SelectItem(cc, phoneCode+""));
	            String code = "+"+phoneCode;
	            phoneCodes.add(new SelectItem(code, code));
	    		
			}		
		}
		return phoneCodes;
	}
	
	public List<SelectItem> getPhoneCodes(List<SelectItem> allCountries) {
		List<SelectItem> items = new ArrayList<>();
		
		for(SelectItem country : allCountries) {
			String cc = country.getValue();
	            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
	            //phoneCodes.add(new SelectItem(cc, phoneCode+""));
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
			System.out.println("currency = "+currency);
			System.out.println("symbol = "+symbol);
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
	

	//private static List<SelectItem> citySelect;
	
//	private Map<String, List<String>> countriesWithCities;
//	
//	public Map<String, List<String>> getCountriesWithCities(){
//		
//		if(countriesWithCities == null) {
//			
//			 countriesWithCities = new HashMap<>();
//			 ObjectMapper mapper = new ObjectMapper();
//			 try {
//				countriesWithCities = new ObjectMapper().readValue(
//						 new ClassPathResource("countriesToCities.json").getFile(), new TypeReference<Map<String, List<String>>>(){});
//				
//				
//			} catch (JsonParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JsonMappingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return countriesWithCities;
//	}
//
//	public List<SelectItem> getCitySelect(String country) {
//		
//		
//		List<String> list = getCountriesWithCities().get(country);
//		if(list == null) return null;
//		
//		List<SelectItem> cities = new ArrayList<>();
//		for(String str : list) {
//			
//			String filterBy = country;
//			cities.add(new SelectItem(str, str, filterBy));
//		}
//		return cities;
//	}

//	public void setCitySelect(List<SelectItem> citySelect) {
//		this.citySelect = citySelect;
//	}
	
	
}

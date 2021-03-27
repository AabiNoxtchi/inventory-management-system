package com.inventory.inventory.Annotations.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.reflections.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.inventory.inventory.Annotations.PhoneNumberAnnotation;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Repository.Interfaces.CountryRepository;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.UsersService;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.jpa.JPAExpressions;

public final class PhoneNumberValidator implements ConstraintValidator<PhoneNumberAnnotation, String> {
	
	@Autowired
	CountryRepository repo;
	
	@Autowired
	UsersService service;
		 
	   // private UserRepository userRepository;
	 
	   /// public UniqueLoginValidator(UserRepository userRepository) {
	   //     this.userRepository = userRepository;
	   // }
	 
	   // public void initialize(UniqueLogin constraint) {
	   // }
	 
	   // public boolean isValid(String login, ConstraintValidatorContext context) {
	  //      return login != null && !userRepository.findByLogin(login).isPresent();
	   // }
	
		public void initialize(PhoneNumberAnnotation constraint) {
	    }

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			// TODO Auto-generated method stub
			if(value==null || Utils.isEmpty(value))return true;			
			
			if(!value.startsWith("+")) {
				
				if(value.startsWith("00")) value = value.replaceFirst("00", "+");
				else{
					if(value.startsWith("0")) value = value.replaceFirst("0", "");
					System.out.println("value =" +value);
					System.out.println("service.getLoggedUser().getId() =" +service.getLoggedUser().getId());
					String phoneCode = (repo.findOne(
				
						QCountry.country.id.in(
						JPAExpressions.selectFrom(QCity.city).select(QCity.city.countryId).where(QCity.city.id.eq(
								JPAExpressions.selectFrom(QMOL.mOL).select(QMOL.mOL.city.id).where(QMOL.mOL.id.eq(service.getLoggedUser().getId()))
								))
						
						))).get().getPhoneCode();
					
					System.out.println("code = "+phoneCode);
					
					value = phoneCode + "/" + value;
				}
				
				
				
//				String phone = model.getPhoneNumber();
//				String currentCode = model.getId()!=null && model.getId() > 0 && phone !=null ?
//						phone.substring(phone.indexOf("+")+1,phone.indexOf("/")):null;
//						System.out.println("current code = "+currentCode);
//						phone = phone==null?null:phone.substring(phone.indexOf("/")+1, phone.length());
//						model.setPhoneNumber(phone);
//				 //model.setSelectedCode();
				
				/*City city = service.getCurrentUserCity();
				String molCountryName = city==null?null:city.getCountry().getName();
				
				List<SelectItem> phoneCodes = new ArrayList<>();
				for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {
					
		            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
		            String displayCountry = (new Locale("", cc)).getDisplayCountry();
		            SelectItem si = new SelectItem(cc, displayCountry+"/+"+phoneCode);
		            phoneCodes.add(si);
		            
		            if((phoneCode+"").equals(currentCode) || displayCountry.equals(molCountryName)) 
		            		{model.setDefaultCodeValue(si);}//model.setSelectedCode(currentCode+"");}
		          //  else if() {model.setDefaultCodeValue(si);}//model.setSelectedCode(phoneCode+"");}
		           
		           // if(molCountryName!=null ) model.setDefaultCodeValue(displayCountry);
		            
		          // System.out.println("cc = "+cc+" code = "+phoneCode+" country = "+displayCountry );
		        }
				model.setPhoneCodes(phoneCodes);*/
				
			}
			
			PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
			PhoneNumber phone = null;
			try {
				phone = phoneNumberUtil.parse(value,
					      CountryCodeSource.UNSPECIFIED.name());
			} catch (NumberParseException e) {
				// TODO Auto-generated catch block
				return false;
			}
			System.out.println("phoneNumberUtil.isValidNumber(phone) = "+phoneNumberUtil.isValidNumber(phone));
			return phoneNumberUtil.isValidNumber(phone);
		}
	 
	

}

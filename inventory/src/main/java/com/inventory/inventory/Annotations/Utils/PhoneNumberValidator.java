package com.inventory.inventory.Annotations.Utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.reflections.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.inventory.inventory.Annotations.PhoneNumberAnnotation;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Repository.Interfaces.CountryRepository;
import com.inventory.inventory.Service.UsersService;
import com.querydsl.jpa.JPAExpressions;

public final class PhoneNumberValidator implements ConstraintValidator<PhoneNumberAnnotation, String> {
	
	@Autowired
	CountryRepository repo;
	
	@Autowired
	UsersService service;
	
		public void initialize(PhoneNumberAnnotation constraint) {
	    }

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			
			if(value == null || Utils.isEmpty(value)) return true;			
			
			if(!value.startsWith("+")) {
				if(service.getLoggedUser() == null) return false;
				
				if(value.startsWith("00")) value = value.replaceFirst("00", "+");
				else{
					if(value.startsWith("0")) value = value.replaceFirst("0", "");
					
					String phoneCode = (repo.findOne(				
						QCountry.country.id.in(
						JPAExpressions.selectFrom(QCity.city).select(QCity.city.countryId).where(QCity.city.id.eq(
								JPAExpressions.selectFrom(QMOL.mOL).select(QMOL.mOL.city.id).where(QMOL.mOL.id.eq(service.getLoggedUser().getId()))
								))						
						))).get().getPhoneCode();
					
					value = phoneCode + "/" + value;
				}
			}
			
			PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
			PhoneNumber phone = null;
			try {
				phone = phoneNumberUtil.parse(value,
					      CountryCodeSource.UNSPECIFIED.name());
			} catch (NumberParseException e) {
					return false;
			}
			
			return phoneNumberUtil.isValidNumber(phone);
		}

}



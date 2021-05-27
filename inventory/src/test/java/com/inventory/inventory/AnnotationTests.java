package com.inventory.inventory;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.inventory.inventory.ViewModels.Supplier.EditVM;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnnotationTests {
	
	 @Autowired
	    private Validator validator;
	    
	    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
	 
	    @Test
	    public void AssertFalsePhoneNumber() throws Exception {
	    	
	    	// given wrong phone number and all else valid 
		      EditVM s = new EditVM();
		      s.setDDCnumber("87yhgt5");	    	
		      s.setName("supplier");
		      s.setPhoneNumber("+548");	      
		    		  
		      Set<ConstraintViolation<EditVM>> violations = validator.validate(s);
		      // then       
		      assertThat(violations.size()).isGreaterThan(0);
		      
		      PhoneNumber phone = phoneNumberUtil.parse("+548909", 
		    	      CountryCodeSource.UNSPECIFIED.name());
		      
		      assertThat(phoneNumberUtil.isValidNumber(phone)).isFalse();
		      
		      PhoneNumber phone2 = phoneNumberUtil.parse("+911234567890", // code for IN
		    	      CountryCodeSource.UNSPECIFIED.name());
		      
		      assertThat(phoneNumberUtil.isValidNumberForRegion(phone2, "US")).isFalse();
	    }
	    
	    @Test
	    public void AssertTruePhoneNumber() throws Exception {
	    	
	    	 // given 
	    	 EditVM s = new EditVM();
	    	 s.setDDCnumber("87yhgt5");
	    	 s.setName("supplier");
	    	 s.setPhoneNumber("+359897018802");
	    	
	    	 Set<ConstraintViolation<EditVM>> violations = validator.validate(s);
		      // then       
		      assertThat(violations.size()).isEqualTo(0);
		      
		      PhoneNumber phone = phoneNumberUtil.parse("+359897014002", 
		    	      CountryCodeSource.UNSPECIFIED.name());
		      
		      assertThat(phoneNumberUtil.isValidNumber(phone)).isTrue();
	     
	    }
	 
	    
	    @Test
	    public void ShouldAssertTruePhoneNumberValid() throws Exception {
	    	
	    	// given 		      
	    	EditVM s = new EditVM();
	    	s.setDDCnumber("87yhgt5");
	    	s.setName("supplier");
	    	s.setPhoneNumber("+911234567890");
	    	
	    	Set<ConstraintViolation<EditVM>> violations = validator.validate(s);
	    	// then
	    	assertThat(violations.size()).isEqualTo(0);
	    	PhoneNumber phone = phoneNumberUtil.parse("+911234567890", 
		    	      CountryCodeSource.UNSPECIFIED.name());
	    	assertThat(phoneNumberUtil.isValidNumber(phone)).isTrue();
		    assertThat(phoneNumberUtil.isValidNumberForRegion(phone, "IN")).isTrue();	
		  
	    }
	    
	    @Test
	    public void AssertTrueSupportedRegionsCode() throws Exception {
	  	
	        int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion("BG");
	        assertThat(phoneCode).isEqualTo(359);
	      
	    }
	    
	    @Test
	    public void AssertTrueEmailValid() throws Exception {
	    	
	    	String email = "user@gmail.com";
	    	// given    	
	    	EditVM s = new EditVM();
	    	s.setDDCnumber("87yhgt5");
	    	s.setName("supplier");
	    	s.setEmail(email);
	    	
	    	Set<ConstraintViolation<EditVM>> violations = validator.validate(s);
	    	
		    // then       
		    assertThat(violations.size()).isEqualTo(0); 			      
		    EmailValidator emailValidator = EmailValidator.getInstance();			      
		    assertThat(emailValidator.isValid(email)).isTrue();
		      
	    }
	    
	    @Test
	    public void ShouldAssertTrueEmailValid() throws Exception {	    	
	    	
	    	EmailValidator emailValidator = EmailValidator.getInstance();	    	
	    	String email = "789.$#*@123mail.com";
	    	String email2 = "name_last@mymail.com";
	    	String email3 = "name.last@mymail.com";
	      
	        assertThat(emailValidator.isValid(email)).isTrue();
	        assertThat(emailValidator.isValid(email2)).isTrue();
	        assertThat(emailValidator.isValid(email3)).isTrue();
	      
	    }
	    
	    @Test
	    public void ShouldAssertFalseEmailValid() throws Exception {	    	
	    	
	    	EmailValidator emailValidator = EmailValidator.getInstance();	    	
	    	String email = "name.last-at-mymail.com";
	    	String email2 = "name.last@mymail";
	    	String email3 = "name.last@mymail.wrongwrong";
	      
	        assertThat(emailValidator.isValid(email)).isFalse();
	        assertThat(emailValidator.isValid(email2)).isFalse();
	        assertThat(emailValidator.isValid(email3)).isFalse();
	      
	    }
	    
	    

}

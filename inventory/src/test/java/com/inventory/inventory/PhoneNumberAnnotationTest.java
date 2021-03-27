package com.inventory.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.inventory.inventory.Model.Supplier;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneNumberAnnotationTest {
 
   
    @Autowired
    private Validator validator;
    
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
 
    @Test
    public void AssertFalsePhoneNumberValid() throws Exception {
        // given    	
    		    //String name, String email, String phoneNumber, String dDCnumber
    		    Supplier s = new Supplier("supplier","supplier@gmail","+91", "87yhgt5");
       
        Set<ConstraintViolation<Supplier>> violations = validator.validate(s);
        // then       
      assertFalse(violations.size()==0);
      
      //PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
      PhoneNumber phone = phoneNumberUtil.parse("+91", 
    	      CountryCodeSource.UNSPECIFIED.name());

      System.out.println("AssertFalsePhoneNumberValid() phoneNumberUtil.isValidNumber(phone) = "+(phoneNumberUtil.isValidNumber(phone)));
      
    }
    
    @Test
    public void AssertTruePhoneNumberValid() throws Exception {
        // given    	
    		    //String name, String email, String phoneNumber, String dDCnumber
    		    Supplier s = new Supplier("supplier","supplier@gmail","00359897014002", "87yhgt5");
       
        Set<ConstraintViolation<Supplier>> violations = validator.validate(s);
        // then       
      assertTrue(violations.size()==0);
      
      PhoneNumber phone = phoneNumberUtil.parse("00359897014002", 
    	      CountryCodeSource.UNSPECIFIED.name());

      System.out.println("AssertTruePhoneNumberValid() phoneNumberUtil.isValidNumber(phone) = "+(phoneNumberUtil.isValidNumber(phone)));
      
    }
 
    
    @Test
    public void ShouldAssertTruePhoneNumberValid() throws Exception {
        // given    	
    		    //String name, String email, String phoneNumber, String dDCnumber
    		    Supplier s = new Supplier("supplier","supplier@gmail","0897014002", "87yhgt5");
       
        Set<ConstraintViolation<Supplier>> violations = validator.validate(s);
        // then       
      assertTrue(violations.size()==0);
      
      PhoneNumber phone = phoneNumberUtil.parse("0897014002", 
    	      CountryCodeSource.UNSPECIFIED.name());

      System.out.println("ShouldAssertTruePhoneNumberValid() phoneNumberUtil.isValidNumber(phone) = "+(phoneNumberUtil.isValidNumber(phone)));
      
    }
    
    @Test
    public void SupportedRegions() throws Exception {
    for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {
		
        int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
       // if(countries.get(cc) != null) countries.get(cc).setPhoneCode(phoneCode+"");
        
		// else if(cc.equals("BG"))BG.setPhoneCode(phoneCode+"");
        
       // String displayCountry = (new Locale("", cc)).getDisplayCountry();
       // SelectItem si = new SelectItem(cc, displayCountry+"/+"+phoneCode);
       // phoneCodes.add(si);
        
       // if((phoneCode+"").equals(currentCode) || displayCountry.equals(molCountryName)) 
        		//{model.setDefaultCodeValue(si);}//model.setSelectedCode(currentCode+"");}
      //  else if() {model.setDefaultCodeValue(si);}//model.setSelectedCode(phoneCode+"");}
       
       // if(molCountryName!=null ) model.setDefaultCodeValue(displayCountry);
        
      System.out.println("cc = "+cc+" code = "+phoneCode );
    }
    }
 
}



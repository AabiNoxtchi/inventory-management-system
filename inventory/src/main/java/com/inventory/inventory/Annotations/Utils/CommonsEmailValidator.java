package com.inventory.inventory.Annotations.Utils;



import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Email;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

import com.inventory.inventory.Annotations.EmailAnnotation;

public class CommonsEmailValidator implements ConstraintValidator<EmailAnnotation, String> {

    private static final boolean ALLOW_LOCAL = false;
    private EmailValidator emailValidator = EmailValidator.getInstance(ALLOW_LOCAL);

    @Override
    public void initialize(EmailAnnotation constraint) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
    	if(value == null) {
    		return true;
    	}
        if (StringUtils.isEmpty(value)) {
          return true;
        }
        if (!emailValidator.isValid(value)) {
            return false;
        }
        return true;
       // return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(value);
      }
}

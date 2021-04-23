package com.inventory.inventory.Annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.inventory.inventory.Annotations.Utils.CommonsEmailValidator;

@Documented
@Constraint(validatedBy = CommonsEmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAnnotation {

  String message() default "email not valid !!!";
  
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
	
}

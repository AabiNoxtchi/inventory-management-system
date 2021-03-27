package com.inventory.inventory.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.inventory.inventory.Annotations.Utils.PhoneNumberValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumberAnnotation {
    String message() default "phone number not valid !!!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

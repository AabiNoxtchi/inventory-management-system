package com.inventory.inventory.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nullable;

@Retention(RetentionPolicy.RUNTIME)


public @interface DropDownAnnotation {	
    public String target();
    public String name();
    public String value();
    public String title();
    public String filterBy() default "";
}
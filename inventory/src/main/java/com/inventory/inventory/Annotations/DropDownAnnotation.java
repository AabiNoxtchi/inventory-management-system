package com.inventory.inventory.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)


public @interface DropDownAnnotation {	
    public String target();
    public String name();
    public String value();
    public String title();
}
package com.inventory.inventory.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EnumAnnotation {
    public String target();
    public String title();

}


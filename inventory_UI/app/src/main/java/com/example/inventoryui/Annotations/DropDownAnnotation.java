package com.example.inventoryui.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DropDownAnnotation {
    public String target();
    public String name();
    public String value();
    public String title();
    public String filterBy() default "";
}
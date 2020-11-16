package com.inventory.inventory.ViewModels.Shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.Predicate;

public abstract class BaseFilterVM 
{  
   private String Prefix ;

   @JsonIgnore
   public abstract Predicate getPredicate();

	public String getPrefix() {
		return Prefix;
	}
	
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}   
   
}
package com.inventory.inventory.ViewModels.Shared;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.Predicate;

public abstract class BaseFilterVM 
{  
   private String Prefix ;
   
  
   protected Map<String,Predicate> dropDownFilters;

   
   @JsonIgnore
   public abstract Predicate getPredicate();
   
   @JsonIgnore
  	public abstract void setDropDownFilters() ; 
   
   
   public abstract Boolean getAll(); 

   public abstract void setAll(Boolean all);
   
	/*
	 * @JsonIgnore public abstract Predicate generateDropDownFilters();
	 */

	public String getPrefix() {
		return Prefix;
	}
	
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}

	 @JsonIgnore
	public Map<String,Predicate> getDropDownFilters() {
		return dropDownFilters;
	}
	 
	 @JsonIgnore
    public Predicate getDropDownPredicate(String predicateName) {
		return dropDownFilters.get(predicateName);
	}
}
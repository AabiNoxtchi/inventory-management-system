package com.inventory.inventory.ViewModels.Shared;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.Predicate;

public abstract class BaseFilterVM 
{  
   private String Prefix ;
   
   @JsonIgnore
   private Boolean filtersSet;
   
   protected Map<String,Predicate> dropDownFilters;
   
   @JsonIgnore
   public abstract Predicate getPredicate();
   
   @JsonIgnore
  	public abstract void setDropDownFilters() ; 
   
   
   public abstract Boolean getAll(); 

   public abstract void setAll(Boolean all);

   public String getPrefix() {
		return Prefix;
	}
	
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	

	 public Boolean getFiltersSet() {
		return filtersSet;
	}

	public void setFiltersSet(Boolean filtersSet) {
		this.filtersSet = filtersSet;
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
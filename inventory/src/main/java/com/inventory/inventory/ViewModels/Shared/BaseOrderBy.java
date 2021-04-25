package com.inventory.inventory.ViewModels.Shared;

import org.springframework.data.domain.Sort;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.OrderSpecifier;

public abstract class BaseOrderBy {
	
	private String Prefix;

    public String getPrefix() {
        if(Prefix == null ) return "OrderBy" ; return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }
    
    @JsonIgnore
    public abstract OrderSpecifier<?> getSpecifier();
	
	@JsonIgnore 
	public abstract Sort getSort();
	
}

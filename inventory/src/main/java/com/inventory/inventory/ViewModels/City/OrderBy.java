package com.inventory.inventory.ViewModels.City;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.querydsl.core.types.OrderSpecifier;

public class OrderBy extends BaseOrderBy{

	@Override 
	public Sort getSort() { 
		return null;
		}
	
	@Override
	@JsonIgnore
	public OrderSpecifier<?> getSpecifier(){
		
		QCity item = QCity.city;
		OrderSpecifier<?> orderBy =  item.id.asc(); 
		return orderBy;
	}

}

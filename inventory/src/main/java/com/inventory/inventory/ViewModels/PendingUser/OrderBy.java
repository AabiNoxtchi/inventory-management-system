package com.inventory.inventory.ViewModels.PendingUser;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.auth.Models.QRegisterRequest;
import com.querydsl.core.types.OrderSpecifier;

public class OrderBy extends BaseOrderBy{
	
	@Override 
	public Sort getSort() { 
		return null;
		}
	
	@Override
	@JsonIgnore
	public OrderSpecifier<?> getSpecifier(){
		
		QRegisterRequest q = QRegisterRequest.registerRequest;
		OrderSpecifier<?> orderBy =  q.id.asc(); 
		return orderBy;
	}

}

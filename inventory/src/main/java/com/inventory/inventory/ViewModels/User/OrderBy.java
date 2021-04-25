package com.inventory.inventory.ViewModels.User;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.QUser;
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
		
		QUser user = QUser.user;
		OrderSpecifier<?> orderBy =  user.id.asc(); 
		return orderBy;
	}

}

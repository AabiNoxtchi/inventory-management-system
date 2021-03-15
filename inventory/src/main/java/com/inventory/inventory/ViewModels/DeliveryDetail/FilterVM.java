package com.inventory.inventory.ViewModels.DeliveryDetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM{

	private Boolean all; 
	
	@JsonIgnore
	private Long userId;
	
	@Override
	public Predicate getPredicate() {
		System.out.println("user id in filter = "+userId);
		Predicate p = QDeliveryDetail.deliveryDetail.product.user.id.eq(userId);				
				  
				System.out.println("p = "+p);
				return p;
		
	}

	@Override
	public void setDropDownFilters() {
		// TODO Auto-generated method stub
		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Override
	public Boolean getAll() {
		
		return all;
	}

	@Override
	public void setAll(Boolean all) {
		this.all = all;
		
	}

}

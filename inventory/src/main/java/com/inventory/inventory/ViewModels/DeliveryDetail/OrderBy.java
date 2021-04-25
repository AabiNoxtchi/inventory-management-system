package com.inventory.inventory.ViewModels.DeliveryDetail;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.querydsl.core.types.OrderSpecifier;

public class OrderBy extends BaseOrderBy{

	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@JsonIgnore
	public OrderSpecifier<?> getSpecifier(){
		
		QDeliveryDetail item = QDeliveryDetail.deliveryDetail;
		OrderSpecifier<?> orderBy =  item.id.asc(); 
		return orderBy;
	}

}

package com.inventory.inventory.ViewModels.Delivery;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.querydsl.core.types.OrderSpecifier;

public class OrderBy extends BaseOrderBy{

	@Override
	public Sort getSort() {
		
		QSort sort = new QSort(QDelivery.delivery.id.asc());  
		return sort;
	}
	
	@Override
	@JsonIgnore
	public OrderSpecifier<?> getSpecifier(){
		
		QDelivery item = QDelivery.delivery;
		OrderSpecifier<?> orderBy =  item.id.asc(); 
		return orderBy;
	}

}

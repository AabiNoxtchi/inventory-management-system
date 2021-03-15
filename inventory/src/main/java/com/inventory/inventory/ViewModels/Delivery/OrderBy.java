package com.inventory.inventory.ViewModels.Delivery;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

public class OrderBy extends BaseOrderBy{

	@Override
	public Sort getSort() {
		
		QSort sort = new QSort(QDelivery.delivery.id.asc());  
		return sort;
	}

}

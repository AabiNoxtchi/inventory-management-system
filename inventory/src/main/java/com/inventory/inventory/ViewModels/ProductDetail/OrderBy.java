package com.inventory.inventory.ViewModels.ProductDetail;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QProductDetail;
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
		
		QProductDetail item = QProductDetail.productDetail;
		OrderSpecifier<?> orderBy =  item.id.asc(); 
		return orderBy;
	}	

}

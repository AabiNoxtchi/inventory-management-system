package com.inventory.inventory.ViewModels.ProductDetail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class Selectable {
	
	private String productName;
	private Long countFree;
	private List<ProductDetailDAO> selectables;
	//private List<SelectItem> inventoryNumbers;
	
	public Selectable() {
		super();
	}

	
	public Selectable(String productName, Long countFree, List<ProductDetailDAO> selectables) {
		super();
		this.productName = productName;
		this.countFree = countFree;
		this.selectables = selectables;
	}


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getCountFree() {
		return countFree;
	}

	public void setCountFree(Long countFree) {
		this.countFree = countFree;
	}

	public List<ProductDetailDAO> getSelectables() {
		return selectables;
	}

	public void setSelectables(List<ProductDetailDAO> selectables) {
		this.selectables = selectables;
	}
	

	
}

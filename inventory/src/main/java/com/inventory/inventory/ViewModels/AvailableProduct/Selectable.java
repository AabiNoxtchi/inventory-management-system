package com.inventory.inventory.ViewModels.AvailableProduct;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QProduct;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class Selectable {
	
	private Long count;
	private List<SelectProduct> selectProducts;
	
	private Long empId;
	
	@JsonIgnore
	private Long userId;
	
	@JsonIgnore
	public Predicate getPredicate() {
		
		System.out.println("userid = "+userId);
		
		Predicate freeProductsP = Expressions.asBoolean(true).isTrue();
//				Expressions.numberTemplate(Long.class, "COALESCE({0},{1})",QProduct.product.employee.id,0).eq((long) 0);
		 
		 Predicate predicate = Expressions.asBoolean(true).isTrue();
//				 QProduct.product.user.id.eq(userId).and(freeProductsP);
		 
		 return predicate;
	}
	
	public Selectable() {
		super();
	}
	public Selectable(Long count, List<SelectProduct> selectProducts) {
		super();
		this.count = count;
		this.selectProducts = selectProducts;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<SelectProduct> getSelectProducts() {
		return selectProducts;
	}
	public void setSelectProducts(List<SelectProduct> selectProducts) {
		this.selectProducts = selectProducts;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

}

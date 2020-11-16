package com.inventory.inventory.ViewModels.Shared;

import org.springframework.data.domain.Sort;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseOrderBy {
	
	@JsonIgnore
	public abstract Sort getSort();
	
	/*
	 * private List<Sort.Order> orders ;
	 * 
	 * public Sort getSort() { if(orders == null ) return null; return
	 * Sort.by(orders); }
	 * 
	 * public List<Sort.Order> getOrders() { return orders; }
	 * 
	 * public void setOrders(List<Sort.Order> orders) { this.orders = orders; }
	 */
	
}

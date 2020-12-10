package com.inventory.inventory.ViewModels.Product;

public class SelectProduct {
	
	private String name;
	private Long totalCount;
	private int count;

	public SelectProduct() {
		super();
	}
	
	public SelectProduct(String name, Long totalCount) {
		super();
		this.name = name;
		this.totalCount = totalCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	

}

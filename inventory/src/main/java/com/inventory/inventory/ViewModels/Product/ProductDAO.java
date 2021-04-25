package com.inventory.inventory.ViewModels.Product;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.UserCategory;

public class ProductDAO {

	private Long id;
	private String name;

	private String description;	
	
	private Long total; 
	
	private UserCategory userCategory;

	public ProductDAO() {
		super();
	}
	
	public ProductDAO(Product p, UserCategory userCategory) {
		this.id=p.getId();
		this.name=p.getName();
		this.description=p.getDescription();
		this.total=p.getTotal();
		this.userCategory=userCategory;				
	}
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public UserCategory getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
	}
	
	
}

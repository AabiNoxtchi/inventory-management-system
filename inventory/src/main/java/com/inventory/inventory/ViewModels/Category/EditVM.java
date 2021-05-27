package com.inventory.inventory.ViewModels.Category;

import javax.validation.constraints.Size;

import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;

public class EditVM extends BaseEditVM<Category>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size(max = 150)
	private String name;	
	private ProductType productType;	
	
	@Override
	public void populateModel(Category item) {
		
		setId(item.getId());
		name=item.getName();
		productType=item.getProductType();		
	}
	
	@Override
	public void populateEntity(Category item) {
	
		item.setId(getId());
		item.setName(name);
		item.setProductType(productType);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	

}
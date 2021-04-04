package com.inventory.inventory.ViewModels.Product;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUserCategory;
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
		QProduct item = QProduct.product;
		OrderSpecifier<?> orderBy =  item.id.asc(); 
		return orderBy;
	}
	
	/*
	 * private String name;
	 * 
	 * private String inventoryNumber;
	 * 
	 * private ProductType productType;
	 * 
	 * private int yearsToDiscard;
	 * 
	 * private boolean isDiscarded;
	 * 
	 * private boolean isAvailable;
	 * 
	 * 
	 * 
	 * public String getName() { return name; }
	 * 
	 * public void setName(String name) { this.name = name; }
	 * 
	 * public String getInventoryNumber() { return inventoryNumber; }
	 * 
	 * public void setInventoryNumber(String inventoryNumber) { this.inventoryNumber
	 * = inventoryNumber; }
	 * 
	 * public ProductType getProductType() { return productType; }
	 * 
	 * public void setProductType(ProductType productType) { this.productType =
	 * productType; }
	 * 
	 * public int getYearsToDiscard() { return yearsToDiscard; }
	 * 
	 * public void setYearsToDiscard(int yearsToDiscard) { this.yearsToDiscard =
	 * yearsToDiscard; }
	 * 
	 * public boolean isDiscarded() { return isDiscarded; }
	 * 
	 * public void setDiscarded(boolean isDiscarded) { this.isDiscarded =
	 * isDiscarded; }
	 * 
	 * public boolean isAvailable() { return isAvailable; }
	 * 
	 * public void setAvailable(boolean isAvailable) { this.isAvailable =
	 * isAvailable; }
	 */

}

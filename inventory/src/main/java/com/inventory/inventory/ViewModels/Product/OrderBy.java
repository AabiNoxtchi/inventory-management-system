package com.inventory.inventory.ViewModels.Product;

import org.springframework.data.domain.Sort;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

public class OrderBy extends BaseOrderBy{
	
	private String name;

	private String inventoryNumber;
	
	private ProductType productType;

	private int yearsToDiscard;

	private boolean isDiscarded;

	private boolean isAvailable;
	
	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public int getYearsToDiscard() {
		return yearsToDiscard;
	}

	public void setYearsToDiscard(int yearsToDiscard) {
		this.yearsToDiscard = yearsToDiscard;
	}

	public boolean isDiscarded() {
		return isDiscarded;
	}

	public void setDiscarded(boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	

}

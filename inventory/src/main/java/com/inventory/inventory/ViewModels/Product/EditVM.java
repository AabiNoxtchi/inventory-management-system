package com.inventory.inventory.ViewModels.Product;

import java.util.Date;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;

public class EditVM extends BaseEditVM<Product>{
	
	@Nullable
	private Long employee_id;
	
	@Nullable
	private Long userId;

	private String name;

	private String inventoryNumber;

	private String description;

	private ProductType productType;

	private int yearsToDiscard;

	private boolean isDiscarded;

	private boolean isAvailable;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreated = new Date();

	// for DMA type
	private Integer amortizationPercent;
	private Integer yearsToMAConvertion;

	@Override
	public void PopulateModel(Product item) {
		
		employee_id = item.getEmployee_id();
		name = item.getName();
		inventoryNumber = item.getInventoryNumber();
		description = item.getDescription();
		productType=item.getProductType();
		yearsToDiscard=item.getYearsToDiscard();
		isDiscarded=item.isDiscarded();
		isAvailable=item.isAvailable();
		dateCreated=item.getDateCreated();
		amortizationPercent=item.getAmortizationPercent();
		yearsToMAConvertion=item.getYearsToMAConvertion();
		
	}

	@Override
	public void PopulateEntity(Product item) {
		
		item.setId(getId());		
		item.setUser(new User(userId));
		if (employee_id != null && employee_id > 0)
			item.setEmployee(new User(employee_id));
		else if (employee_id == null || employee_id == 0)
			item.setEmployee(null);
		item.setName(name);
		item.setDescription(description);
		item.setInventoryNumber(inventoryNumber);		
		item.setProductType(productType);
		item.setYearsToDiscard(yearsToDiscard);
		item.setDiscarded(isDiscarded);
		item.setAvailable(isAvailable);
		item.setDateCreated(dateCreated);
		item.setAmortizationPercent(amortizationPercent);
		item.setYearsToMAConvertion(yearsToMAConvertion);
		
	}
	
	 public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public Long getEmployee_id(){
	        return employee_id;
	    }

	    public String getInventoryNumber() {
	        return inventoryNumber;
	    }

	    public void setInventoryNumber(String inventoryNumber) {
	        this.inventoryNumber = inventoryNumber;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
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

	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	    public Date getDateCreated() {
	        return dateCreated;
	    }

	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	    public void setDateCreated(Date dateCreated) {
	        this.dateCreated = dateCreated;
	    }

	    public Integer getAmortizationPercent() {
	        return amortizationPercent;
	    }

	    public void setAmortizationPercent(Integer amortizationPercent) {
	        this.amortizationPercent = amortizationPercent;
	    }

	    public Integer getYearsToMAConvertion() {
	        return yearsToMAConvertion;
	    }

	    public void setYearsToMAConvertion(Integer yearsToMAConvertion) {
	        this.yearsToMAConvertion = yearsToMAConvertion;
	    }

	    public void setEmployee_id(Long employee_id) {
	        this.employee_id = employee_id;
	    }

	    @JsonIgnore
		public Long getUserId() {
			return userId;
		}

	    @JsonIgnore
		public void setUserId(Long userId) {
			this.userId = userId;
		}

}

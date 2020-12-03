package com.example.inventoryui.Models.Product;

import com.example.inventoryui.Models.Shared.BaseEditVM;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class EditVM extends BaseEditVM<Product> {
	
	private Long employeeId;

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

	public Date getDateCreated() {
		return dateCreated;
	}

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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}


}

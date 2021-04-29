package com.example.inventoryui.Models.Inventory;

import com.example.inventoryui.Models.Shared.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Inventory extends BaseModel implements Serializable {


	private String inventoryNumber;
	
	private boolean isDiscarded;

	private ECondition econdition;
	
	private String productName;
	
	private ProductType productType;
	
	private double amortizationPercent = 0;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateCreated;
	
	private Long deliveryNumber;
	
	private Long deliveryDetailId;
	
	private BigDecimal price ;
	private Double totalAmortizationPercent=0.0;
	private BigDecimal totalAmortization;


	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public boolean isDiscarded() {
		return isDiscarded;
	}

	public void setDiscarded(boolean discarded) {
		isDiscarded = discarded;
	}

	public ECondition getEcondition() {
		return econdition;
	}

	public void setEcondition(ECondition econdition) {
		this.econdition = econdition;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getDeliveryNumber() {
		return deliveryNumber;
	}

	public void setDeliveryNumber(Long deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}

	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Double getTotalAmortizationPercent() {
		return totalAmortizationPercent;
	}

	public void setTotalAmortizationPercent(Double totalAmortizationPercent) {
		this.totalAmortizationPercent = totalAmortizationPercent;
	}

	public BigDecimal getTotalAmortization() {
		return totalAmortization;
	}

	public void setTotalAmortization(BigDecimal totalAmortization) {
		this.totalAmortization = totalAmortization;
	}
}



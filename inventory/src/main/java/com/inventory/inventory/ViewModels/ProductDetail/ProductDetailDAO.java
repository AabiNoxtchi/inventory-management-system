package com.inventory.inventory.ViewModels.ProductDetail;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.UserCategory;

public class ProductDetailDAO {

	private Long id;
	private String inventoryNumber;
	
	private boolean isDiscarded;

	private ECondition econdition;
	
	private String productName;
	
	private ProductType productType;
	
	private double amortizationPercent = 0;
	
	private LocalDate dateCreated;
	
	private Long deliveryNumber;
	
	private Long deliveryDetailId;
	
	private BigDecimal price ;
	
	@JsonIgnore
	private Double totalAmortizationPercent; 
	private BigDecimal totalAmortization;
	
		
	public ProductDetailDAO(ProductDetail pd, String name, UserCategory uc, 
			LocalDate date, Long number, BigDecimal price) { 
		this.id = pd.getId();
		this.inventoryNumber = pd.getInventoryNumber();
		this.isDiscarded = pd.isDiscarded();
		this.econdition = pd.getEcondition();
		this.deliveryDetailId = pd.getDeliveryDetailId();
		
		this.productName= name;
		this.productType= uc.getCategory().getProductType();
		this.amortizationPercent= uc.getAmortizationPercent();
		this.dateCreated= date;
		this.deliveryNumber=number;
		this.price= price;
		this.totalAmortizationPercent = pd.getTotalAmortizationPercent();
		totalAmortization();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInventoryNumber() {
		return inventoryNumber;
	}
	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}
	public boolean isDiscarded() {
		return isDiscarded;
	}
	public void setDiscarded(boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}
	public String getProductName() {
		return productName;
	}
	public ECondition getEcondition() {
		return econdition;
	}
	public void setEcondition(ECondition econdition) {
		this.econdition = econdition;
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
	public LocalDate getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Long getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(Long deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}
	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}
	public Double getTotalAmortizationPercent() {
		return totalAmortizationPercent;
	}
	public BigDecimal getTotalAmortization() {
		return totalAmortization;
	}
	public void setTotalAmortization(BigDecimal totalAmortization) {
		this.totalAmortization = totalAmortization;
	}
	public void setTotalAmortizationPercent(Double totalAmortizationPercent) {
		this.totalAmortizationPercent = totalAmortizationPercent;
	}

	private void totalAmortization() {
		try {
			
			Double percent = totalAmortizationPercent/100.0;
			BigDecimal amount = BigDecimal.valueOf(percent);
			this.totalAmortization = price.multiply(amount);
			this.totalAmortization = this.totalAmortization.setScale(2,BigDecimal.ROUND_HALF_UP);
			
		}catch(Exception e){}
	}	
	
	@JsonIgnore
	public ProductDetail getProductDetail() {
		
		ProductDetail pd = new ProductDetail(inventoryNumber, isDiscarded, econdition, new DeliveryDetail(deliveryDetailId));
		pd.setId(id);
		pd.setTotalAmortizationPercent(totalAmortizationPercent);
		return pd;
	}

}



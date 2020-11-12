package com.inventory.inventory.ViewModels.Product;

import java.util.Date;

import org.hibernate.type.StandardBasicTypes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProduct;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM {
	
	private String name;
	private Long userId;
	private Long employeeId;
	private String inventoryNumber;
	// private String description;
	private ProductType productType;
	private Boolean isDiscarded;
	private Boolean isAvailable;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreatedBefore;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreatedAfter;
	private Integer yearsToDiscardFromStartMoreThan;
	private Integer yearsToDiscardFromStartLessThan;
	private Integer yearsToDiscardFromNowMoreThan;
	private Integer yearsToDiscardFromNowLessThan;
	// for DMA type
	private Integer amortizationPercent;
	// for DMA type
	private Integer yearsToMAConvertion;
	
    public Predicate getPredicate() {
    	Date date=new Date();
    	long now = new Date().getTime();
	  Predicate predicate =( 
			  name == null ? Expressions.asBoolean(true).isTrue()
				  		   : QProduct.product.name.toLowerCase().contains(name.toLowerCase()))
			  .and( userId == null ? Expressions.asBoolean(true).isTrue()
					  	    : QProduct.product.user.id.eq(userId)) 
			  .and( employeeId == null ? Expressions.asBoolean(true).isTrue()
					  		: QProduct.product.employee.id.eq(employeeId)) 
			  .and( inventoryNumber == null ? Expressions.asBoolean(true).isTrue()
					  		:QProduct.product.inventoryNumber.contains(inventoryNumber)) 
			  .and( productType == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.productType.eq(productType)) 
			  .and( isDiscarded == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.isDiscarded.eq(isDiscarded)) 
			  .and( isAvailable == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.isAvailable.eq(isAvailable)) 
			  .and( dateCreatedBefore == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.dateCreated.before(dateCreatedBefore)) 
			  .and( dateCreatedAfter == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.dateCreated.after(dateCreatedAfter))
			  .and( yearsToDiscardFromStartMoreThan == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.yearsToDiscard.gt(yearsToDiscardFromStartMoreThan))
			  .and( yearsToDiscardFromStartLessThan == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.yearsToDiscard.lt(yearsToDiscardFromStartLessThan))		
			 
					  			;
	  
	  return predicate; 
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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
	public Boolean getIsDiscarded() {
		return isDiscarded;
	}
	public void setIsDiscarded(Boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public Date getDateCreatedBefore() {
		return dateCreatedBefore;
	}
	public void setDateCreatedBefore(Date dateCreatedBefore) {
		this.dateCreatedBefore = dateCreatedBefore;
	}
	public Date getDateCreatedAfter() {
		return dateCreatedAfter;
	}
	public void setDateCreatedAfter(Date dateCreatedAfter) {
		this.dateCreatedAfter = dateCreatedAfter;
	}
	public Integer getYearsToDiscardFromStartMoreThan() {
		return yearsToDiscardFromStartMoreThan;
	}
	public void setYearsToDiscardFromStartMoreThan(Integer yearsToDiscardFromStartMoreThan) {
		this.yearsToDiscardFromStartMoreThan = yearsToDiscardFromStartMoreThan;
	}
	public Integer getYearsToDiscardFromStartLessThan() {
		return yearsToDiscardFromStartLessThan;
	}
	public void setYearsToDiscardFromStartLessThan(Integer yearsToDiscardFromStartLessThan) {
		this.yearsToDiscardFromStartLessThan = yearsToDiscardFromStartLessThan;
	}
	public Integer getYearsToDiscardFromNowMoreThan() {
		return yearsToDiscardFromNowMoreThan;
	}
	public void setYearsToDiscardFromNowMoreThan(Integer yearsToDiscardFromNowMoreThan) {
		this.yearsToDiscardFromNowMoreThan = yearsToDiscardFromNowMoreThan;
	}
	public Integer getYearsToDiscardFromNowLessThan() {
		return yearsToDiscardFromNowLessThan;
	}
	public void setYearsToDiscardFromNowLessThan(Integer yearsToDiscardFromNowLessThan) {
		this.yearsToDiscardFromNowLessThan = yearsToDiscardFromNowLessThan;
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
	
}

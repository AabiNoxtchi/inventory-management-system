package com.inventory.inventory.ViewModels.Product;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM{ 
	
	private String name;
	private Long userId;
	private Long employeeId;
	private Boolean freeProducts; 
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
	private Integer yearsLeftToDiscardMoreThan;
	private Integer yearsLeftToDiscardLessThan;
	// for DMA type
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;
	// for DMA type
	private Integer yearsToMAConvertionMoreThan;
	private Integer yearsToMAConvertionLessThan;
	private Integer yearsLeftToMAConvertionMoreThan;
	private Integer yearsLeftToMAConvertionLessThan;
	private List<Long> ids;
	
	
    public Predicate getPredicate() {
		
		Predicate freeProductsP = Expressions.numberTemplate(Long.class, "COALESCE({0},{1})",QProduct.product.employee.id,0).eq((long) 0);
        
      LocalDate date = LocalDate.now();
	  Predicate predicate =( (
			  name == null ? Expressions.asBoolean(true).isTrue()
				  		   : QProduct.product.name.toLowerCase().contains(name.toLowerCase()))
			  .and( userId == null ? Expressions.asBoolean(true).isTrue()
					  	    : QProduct.product.user.id.eq(userId)) 
			  .and( employeeId == null ? Expressions.asBoolean(true).isTrue()
					  		: QProduct.product.employee.id.eq(employeeId) )					  			 
			  .and( freeProducts == null ? Expressions.asBoolean(true).isTrue()
					  : freeProductsP )			 
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
			  .and( yearsLeftToDiscardMoreThan == null ? Expressions.asBoolean(true).isTrue() 
				  		: QProduct.product.yearsToDiscard
				  		.subtract(Expressions.numberTemplate
				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			
				  		.gt(yearsLeftToDiscardMoreThan))
			  .and( yearsLeftToDiscardLessThan == null ? Expressions.asBoolean(true).isTrue() 
				  		: QProduct.product.isDiscarded.eq(false).and( QProduct.product.yearsToDiscard
				  		.subtract(Expressions.numberTemplate
				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			  					
				  		.lt(yearsLeftToDiscardLessThan)))
			  .and(amortizationPercentMoreThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.amortizationPercent.gt(amortizationPercentMoreThan))
			  .and(amortizationPercentLessThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.productType.eq(ProductType.DMA).and(
						  QProduct.product.amortizationPercent.lt(amortizationPercentLessThan)))
			  .and(yearsToMAConvertionMoreThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.yearsToMAConvertion.gt(yearsToMAConvertionMoreThan))
			  .and(yearsToMAConvertionLessThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.productType.eq(ProductType.DMA).and(
						  QProduct.product.yearsToMAConvertion.lt(yearsToMAConvertionLessThan)))
			  .and( yearsLeftToMAConvertionMoreThan == null ? Expressions.asBoolean(true).isTrue() 
				  		: QProduct.product.yearsToMAConvertion
				  		.subtract(Expressions.numberTemplate
				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			
				  		.gt(yearsLeftToMAConvertionMoreThan))
			  .and( yearsLeftToMAConvertionLessThan == null ? Expressions.asBoolean(true).isTrue() 
				  		: QProduct.product.productType.eq(ProductType.DMA).and( QProduct.product.yearsToMAConvertion
				  		.subtract(Expressions.numberTemplate
				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			  					
				  		.lt(yearsLeftToMAConvertionLessThan)))
			  .and( ids == null || ids.size()<1 ? Expressions.asBoolean(true).isTrue() 
					  : QProduct.product.id.in(ids))
				  );
	  return predicate; 
    }
    
    //******** getters and setters ********//	
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

	public Integer getYearsLeftToDiscardMoreThan() {
		return yearsLeftToDiscardMoreThan;
	}

	public void setYearsLeftToDiscardMoreThan(Integer yearsLeftToDiscardMoreThan) {
		this.yearsLeftToDiscardMoreThan = yearsLeftToDiscardMoreThan;
	}

	public Integer getYearsLeftToDiscardLessThan() {
		return yearsLeftToDiscardLessThan;
	}

	public void setYearsLeftToDiscardLessThan(Integer yearsLeftToDiscardLessThan) {
		this.yearsLeftToDiscardLessThan = yearsLeftToDiscardLessThan;
	}

	public Integer getAmortizationPercentMoreThan() {
		return amortizationPercentMoreThan;
	}

	public void setAmortizationPercentMoreThan(Integer amortizationPercentMoreThan) {
		this.amortizationPercentMoreThan = amortizationPercentMoreThan;
	}

	public Integer getAmortizationPercentLessThan() {
		return amortizationPercentLessThan;
	}

	public void setAmortizationPercentLessThan(Integer amortizationPercentLessThan) {
		this.amortizationPercentLessThan = amortizationPercentLessThan;
	}

	public Integer getYearsToMAConvertionMoreThan() {
		return yearsToMAConvertionMoreThan;
	}

	public void setYearsToMAConvertionMoreThan(Integer yearsToMAConvertionMoreThan) {
		this.yearsToMAConvertionMoreThan = yearsToMAConvertionMoreThan;
	}

	public Integer getYearsToMAConvertionLessThan() {
		return yearsToMAConvertionLessThan;
	}

	public void setYearsToMAConvertionLessThan(Integer yearsToMAConvertionLessThan) {
		this.yearsToMAConvertionLessThan = yearsToMAConvertionLessThan;
	}

	public Integer getYearsLeftToMAConvertionMoreThan() {
		return yearsLeftToMAConvertionMoreThan;
	}

	public void setYearsLeftToMAConvertionMoreThan(Integer yearsLeftToMAConvertionMoreThan) {
		this.yearsLeftToMAConvertionMoreThan = yearsLeftToMAConvertionMoreThan;
	}

	public Integer getYearsLeftToMAConvertionLessThan() {
		return yearsLeftToMAConvertionLessThan;
	}

	public void setYearsLeftToMAConvertionLessThan(Integer yearsLeftToMAConvertionLessThan) {
		this.yearsLeftToMAConvertionLessThan = yearsLeftToMAConvertionLessThan;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public Boolean getFreeProducts() {
		return freeProducts;
	}

	public void setFreeProducts(Boolean freeProducts) {
		this.freeProducts = freeProducts;
	}
	
	
	
	
}


package com.inventory.inventory.ViewModels.ProductDetail;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

public class FilterVM extends BaseFilterVM{ 
	
	
	private Boolean all;	
	private Long userId;	
		
	private BigDecimal priceMoreThan;	
	private BigDecimal priceLessThan;
	private Boolean isDiscarded;
	private Boolean isAvailable;
	
	private Long deliveryDetailId;
	
	@DropDownAnnotation(target="deliveryId",value="delivery.id",name="delivery.number",title="select delivery")
	private List<SelectItem> deliveryNumbers; // name = number
	private Long deliveryId;

	@DropDownAnnotation(target="producId",value="product.id",name="product.name",title="select product")
	private List<SelectItem> productNames;
	private Long productId;
	
	@DropDownAnnotation(target="id",value="id",name="inventoryNumber",title="select number", filterBy="productId")
	private List<SelectItem> inventoryNumbers;
	//private String inventoryNumber;
	private Long id;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateCreatedBefore;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateCreatedAfter;

	// for DMA type
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;
	
	
	private Boolean freeInventory;
	private List<Long> notIn;
	
/*
	//private Boolean freeProducts; 
	//private Boolean employeeIdOrFree;
	
//	private Integer yearsToDiscardFromStartMoreThan;
//	private Integer yearsToDiscardFromStartLessThan;
//	private Integer yearsLeftToDiscardMoreThan;
//	private Integer yearsLeftToDiscardLessThan;
	
	//@DropDownAnnotation(target="employeeId",value="user.id",name="user.userName",title="select employee")
	//private List<SelectItem> employeenames;
	//private Long employeeId;
	
	// for DMA type
//	private Integer yearsToMAConvertionMoreThan;
//	private Integer yearsToMAConvertionLessThan;
//	private Integer yearsLeftToMAConvertionMoreThan;
//	private Integer yearsLeftToMAConvertionLessThan;
//	private List<Long> discardedFromServerIds;
	//discardedFromServerIds
	//discardedFromServerIds
	//discardedFromServerIds
	 * 
	 */
	/*
	 * public Predicate getFreeProductsPredicate() { return
	 * Expressions.numberTemplate(Long.class,
	 * "COALESCE({0},{1})",QProduct.product.employee.id,0).eq((long) 0); }
	 */
    
	public Predicate getPredicate() {
		
	 // Predicate freeProductsP = Expressions.numberTemplate(Long.class, "COALESCE({0},{1})",QProduct.product.employee.id,0).eq((long) 0);
        
      LocalDate date = LocalDate.now();
      System.out.println("(freeInventory == null||!freeInventory ) ="+(freeInventory == null||!freeInventory ));
	  Predicate predicate = 
//			  employeeIdOrFree != null && employeeId !=null && userId != null ? 
//					  (QProduct.product.employee.id.eq(employeeId).or(freeProductsP)).and(QProduct.product.user.id.eq(userId))
//					  :
//			 ( name == null ? Expressions.asBoolean(true).isTrue()
//				  		   : QProduct.product.name.toLowerCase().contains(name.toLowerCase()))
//			  .and
			  (freeInventory == null||!freeInventory ? Expressions.asBoolean(true).isTrue()
					  :	(QProductDetail.productDetail.id.in(
						  JPAExpressions.selectFrom(QUserProfile.userProfile)
						  .where(QUserProfile.userProfile.userId.eq(userId)
								  .and(QUserProfile.userProfile.returnedAt.isNull()))
						  .select(QUserProfile.userProfile.productDetail.id)
						  )).and(notIn == null ? Expressions.asBoolean(true).isTrue(): QProductDetail.productDetail.id.notIn(notIn))	
					  ).and
			  (id == null ? Expressions.asBoolean(true).isTrue()
					  :  QProductDetail.productDetail.id.eq(id))
			  		.and(userId == null ? null
					  	    : QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(userId))
			  		.and(priceMoreThan == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.pricePerOne.gt(priceMoreThan))
			  		.and(priceLessThan == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.pricePerOne.lt(priceLessThan))
			  		.and( isDiscarded == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.isDiscarded.eq(isDiscarded)) 
			  		.and( isAvailable == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.isAvailable.eq(isAvailable)) 
			  		.and(deliveryDetailId == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.id.eq(deliveryDetailId))
			  		.and(deliveryId == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.delivery.id.eq(deliveryId))
			  		.and(productId == null ? Expressions.asBoolean(true).isTrue() 
			  				: QProductDetail.productDetail.deliveryDetail.product.id.eq(productId))
			  		/*.and( inventoryNumber == null ? Expressions.asBoolean(true).isTrue()
					  		:QProductDetail.productDetail.inventoryNumber.contains(inventoryNumber))*/ 
			  .and( productType == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.deliveryDetail.product.userCategory.category.productType.eq(productType)) 			  
			  .and( dateCreatedBefore == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.deliveryDetail.delivery.date.before(dateCreatedBefore)) 
			  .and( dateCreatedAfter == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.deliveryDetail.delivery.date.after(dateCreatedAfter))
			  .and(amortizationPercentMoreThan == null ? Expressions.asBoolean(true).isTrue()
			  :  QProductDetail.productDetail.deliveryDetail.product.userCategory.category.productType.eq(ProductType.LTA).and(
					  QProductDetail.productDetail.deliveryDetail.product.userCategory.amortizationPercent.gt(amortizationPercentMoreThan)))
	  .and(amortizationPercentLessThan ==null ? Expressions.asBoolean(true).isTrue()
			  : QProductDetail.productDetail.deliveryDetail.product.userCategory.category.productType.eq(ProductType.LTA).and(
					  QProductDetail.productDetail.deliveryDetail.product.userCategory.amortizationPercent.lt(amortizationPercentLessThan))
			    
					  
			  );
			  /*.and( employeeId == null ? Expressions.asBoolean(true).isTrue()
					  		: QProductDetail.productDetail.user.id.eq(employeeId) )	;*/				  			 
//			  .and( freeProducts == null ? Expressions.asBoolean(true).isTrue()
//					  : freeProductsP )			 
//			  
//			  .and( yearsToDiscardFromStartMoreThan == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.isDiscarded.eq(false).and(QProduct.product.yearsToDiscard.gt(yearsToDiscardFromStartMoreThan)))
//			  .and( yearsToDiscardFromStartLessThan == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.isDiscarded.eq(false).and(QProduct.product.yearsToDiscard.lt(yearsToDiscardFromStartLessThan)))	
//			  .and( yearsLeftToDiscardMoreThan == null ? Expressions.asBoolean(true).isTrue() 
//				  		: QProduct.product.isDiscarded.eq(false).and(QProduct.product.yearsToDiscard
//				  		.subtract(Expressions.numberTemplate
//				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			
//				  		.gt(yearsLeftToDiscardMoreThan)))
//			  .and( yearsLeftToDiscardLessThan == null ? Expressions.asBoolean(true).isTrue() 
//				  		: QProduct.product.isDiscarded.eq(false).and( QProduct.product.yearsToDiscard
//				  		.subtract(Expressions.numberTemplate
//				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			  					
//				  		.lt(yearsLeftToDiscardLessThan)))

//			  .and(yearsToMAConvertionMoreThan ==null ? Expressions.asBoolean(true).isTrue()
//					  : QProduct.product.yearsToMAConvertion.gt(yearsToMAConvertionMoreThan))
//			  .and(yearsToMAConvertionLessThan ==null ? Expressions.asBoolean(true).isTrue()
//					  : QProduct.product.productType.eq(ProductType.DMA).and(
//						  QProduct.product.yearsToMAConvertion.lt(yearsToMAConvertionLessThan)))
//			  .and( yearsLeftToMAConvertionMoreThan == null ? Expressions.asBoolean(true).isTrue() 
//				  		: QProduct.product.yearsToMAConvertion
//				  		.subtract(Expressions.numberTemplate
//				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			
//				  		.gt(yearsLeftToMAConvertionMoreThan))
//			  .and( yearsLeftToMAConvertionLessThan == null ? Expressions.asBoolean(true).isTrue() 
//				  		: QProduct.product.productType.eq(ProductType.DMA).and( QProduct.product.yearsToMAConvertion
//				  		.subtract(Expressions.numberTemplate
//				  					( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))			  					
//				  		.lt(yearsLeftToMAConvertionLessThan)))
//			  .and( discardedFromServerIds == null || discardedFromServerIds.size() < 1 ? Expressions.asBoolean(true).isTrue() 
//					  : QProduct.product.id.in(discardedFromServerIds));
//				
			  	
	  return predicate;
    }
    
    @SuppressWarnings("serial")
	@Override
	public void setDropDownFilters() {
    	
		Predicate productDts = 
				userId != null ? 
				QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(userId)
				: Expressions.asBoolean(true).isTrue() ;
				Predicate products = 
						userId != null ? 
						QProduct.product.userCategory.user.id.eq(userId)
						: Expressions.asBoolean(true).isTrue() ;				
				
						Predicate deliveries = userId != null ? 
								QDelivery.delivery.id.in(JPAExpressions.selectFrom(QDeliveryDetail.deliveryDetail)
										.where(QDeliveryDetail.deliveryDetail.product.userCategory.userId.eq(userId))
										.distinct()
										.select(QDeliveryDetail.deliveryDetail.delivery.id))
								: Expressions.asBoolean(true).isTrue() ;
//				employeeId != null ? 
//			  		 QProductDetail.productDetail.user.id.eq( employeeId) 
//			  		 : null ;
//		Predicate employeenames = 
//				userId != null ? 
//				QUser.user.mol.id.eq(userId)	 
//				: null;
					
		dropDownFilters = new HashMap<String, Predicate>() {{		
			  put("productNames", products);
			  put("deliveryNumbers", deliveries);
			  put("inventoryNumbers",productDts);
		 }};				
	}

    
  //******** getters and setters ********//	
	public Boolean getAll() {
		return all;
	}
	public void setAll(Boolean all) {
		this.all = all;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}	
	
	public BigDecimal getPriceMoreThan() {
		return priceMoreThan;
	}

	public void setPriceMoreThan(BigDecimal priceMoreThan) {
		this.priceMoreThan = priceMoreThan;
	}

	public BigDecimal getPriceLessThan() {
		return priceLessThan;
	}

	public void setPriceLessThan(BigDecimal priceLessThan) {
		this.priceLessThan = priceLessThan;
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
	
	public List<SelectItem> getInventoryNumbers() {
		return inventoryNumbers;
	}
	public void setInventoryNumbers(List<SelectItem> inventoryNumbers) {
		this.inventoryNumbers = inventoryNumbers;
	}
	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}
	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}	
	
	public List<SelectItem> getDeliveryNumbers() {
		return deliveryNumbers;
	}

	public void setDeliveryNumbers(List<SelectItem> deliveryNumbers) {
		this.deliveryNumbers = deliveryNumbers;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public List<SelectItem> getProductNames() {
		return productNames;
	}
	public void setProductNames(List<SelectItem> productNames) {
		this.productNames = productNames;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public List<SelectItem> getProductTypes() {
		return productTypes;
	}
	public void setProductTypes(List<SelectItem> productTypes) {
		this.productTypes = productTypes;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
//	public Date getDateCreatedBefore() {
//		return dateCreatedBefore;
//	}
//	public void setDateCreatedBefore(Date dateCreatedBefore) {
//		this.dateCreatedBefore = dateCreatedBefore;
//	}
//	public Date getDateCreatedAfter() {
//		return dateCreatedAfter;
//	}
//	public void setDateCreatedAfter(Date dateCreatedAfter) {
//		this.dateCreatedAfter = dateCreatedAfter;
//	}
	
	public Integer getAmortizationPercentMoreThan() {
		return amortizationPercentMoreThan;
	}
	public LocalDate getDateCreatedBefore() {
		return dateCreatedBefore;
	}

	public void setDateCreatedBefore(LocalDate dateCreatedBefore) {
		this.dateCreatedBefore = dateCreatedBefore;
	}

	public LocalDate getDateCreatedAfter() {
		return dateCreatedAfter;
	}

	public void setDateCreatedAfter(LocalDate dateCreatedAfter) {
		this.dateCreatedAfter = dateCreatedAfter;
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

	public Boolean getFreeInventory() {
		return freeInventory;
	}

	public void setFreeInventory(Boolean freeInventory) {
		this.freeInventory = freeInventory;
	}
	
	

	public List<Long> getNotIn() {
		return notIn;
	}

	public void setNotIn(List<Long> notIn) {
		this.notIn = notIn;
	}

	@Override
	public String toString() {
		return "FilterVM [all=" + all + ", userId=" + userId + ", priceMoreThan=" + priceMoreThan + ", priceLessThan="
				+ priceLessThan + ", isDiscarded=" + isDiscarded + ", isAvailable=" + isAvailable
				+ ", deliveryDetailId=" + deliveryDetailId + ", deliveryNumbers=" + deliveryNumbers + ", deliveryId="
				+ deliveryId + ", productNames=" + productNames + ", productId=" + productId + ", inventoryNumbers="
				+ inventoryNumbers + ", id=" + id + ", productTypes=" + productTypes + ", productType=" + productType
				+ ", dateCreatedBefore=" + dateCreatedBefore + ", dateCreatedAfter=" + dateCreatedAfter
				+ ", amortizationPercentMoreThan=" + amortizationPercentMoreThan + ", amortizationPercentLessThan="
				+ amortizationPercentLessThan + ", freeInventory=" + freeInventory + ", notIn=" + notIn + "]";
	}

	
	
    
    //******** getters and setters ********//	
    
    
}
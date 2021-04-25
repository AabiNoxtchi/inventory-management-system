package com.inventory.inventory.ViewModels.ProductDetail;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

public class FilterVM extends BaseFilterVM{ 
	
	
	private Boolean all;	
	private Long userId;
	private Long employeeId;
		
	private BigDecimal priceMoreThan;	
	private BigDecimal priceLessThan;
	private Boolean isDiscarded;
	
	@EnumAnnotation(target="econdition",title="select condition")
	private List<SelectItem> econditions;
	private ECondition econdition;
	
	private Long deliveryDetailId;
	
	@DropDownAnnotation(target="deliveryId",value="delivery.id",name="delivery.number",title="select delivery")
	private List<SelectItem> deliveryNumbers; 
	private Long deliveryId;

	@DropDownAnnotation(target="producId",value="product.id",name="product.name",title="select product")
	private List<SelectItem> productNames;
	private Long productId;
	
	@DropDownAnnotation(target="id",value="id",name="inventoryNumber",title="select number", filterBy="productId")
	private List<SelectItem> inventoryNumbers;	
	private Long id;
	
	private List<Long> ids;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateCreatedBefore;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateCreatedAfter;

	// for DMA type
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;
	
	
	private Boolean freeInventory;
	private List<Long> notIn;
    
	public Predicate getPredicate() {
		
	  Predicate predicate =
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
					  .and(ids == null ? Expressions.asBoolean(true).isTrue() : QProductDetail.productDetail.id.in(ids))
			  		.and(userId != null ?
					  	     QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(userId) : 
			  				QProductDetail.productDetail.id.in(
			  						JPAExpressions.selectFrom(QUserProfile.userProfile)
			  								.where(QUserProfile.userProfile.userId.eq(employeeId).and(QUserProfile.userProfile.returnedAt.isNull()))
			  								.select(QUserProfile.userProfile.productDetailId)
			  						))
			  		.and(priceMoreThan == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.pricePerOne.gt(priceMoreThan))
			  		.and(priceLessThan == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.pricePerOne.lt(priceLessThan))
			  		.and( isDiscarded == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.isDiscarded.eq(isDiscarded)) 			  		
			  		.and( econdition == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.econdition.eq(econdition))			  		
			  		.and(deliveryDetailId == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.id.eq(deliveryDetailId))
			  		.and(deliveryId == null ? Expressions.asBoolean(true).isTrue()
			  				: QProductDetail.productDetail.deliveryDetail.delivery.id.eq(deliveryId))
			  		.and(productId == null ? Expressions.asBoolean(true).isTrue() 
			  				: QProductDetail.productDetail.deliveryDetail.product.id.eq(productId))			  		
			  .and( productType == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProductDetail.productDetail.deliveryDetail.product.userCategory.id.in(
					  				JPAExpressions.selectFrom(QUserCategory.userCategory)
					  				.where(QUserCategory.userCategory.userId.eq(userId)
					  						.and(QUserCategory.userCategory.category.productType.eq(productType)))
					  				.select(QUserCategory.userCategory.id)
					  				))
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
			  	
	  return predicate;
    }
    
    @SuppressWarnings("serial")
	@Override
	public void setDropDownFilters() {
    	
		Predicate productDts = 
				userId != null ? 
				QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(userId)
				: null;
				Predicate products = 
						userId != null ? 
						QProduct.product.userCategory.user.id.eq(userId)
						: null;			
				
						Predicate deliveries = userId != null ? 
								QDelivery.delivery.id.in(JPAExpressions.selectFrom(QDeliveryDetail.deliveryDetail)
										.where(QDeliveryDetail.deliveryDetail.product.userCategory.userId.eq(userId))
										.distinct()
										.select(QDeliveryDetail.deliveryDetail.delivery.id))
								: null;
					
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
	public Integer getAmortizationPercentMoreThan() {
		return amortizationPercentMoreThan;
	}
	public List<SelectItem> getEconditions() {
		return econditions;
	}
	public void setEconditions(List<SelectItem> econditions) {
		this.econditions = econditions;
	}
	public ECondition getEcondition() {
		return econdition;
	}
	public void setEcondition(ECondition econdition) {
		this.econdition = econdition;
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
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {
		// TODO Auto-generated method stub
		return QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(userId).and(QProductDetail.productDetail.id.eq(id));
	}

	@Override
	public Predicate getListAuthorizationPredicate(List<Long> ids, ERole eRole, Long userId) {
		// TODO Auto-generated method stub
		return QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(userId).and(QProductDetail.productDetail.id.in(ids));
	}	
    
    
}


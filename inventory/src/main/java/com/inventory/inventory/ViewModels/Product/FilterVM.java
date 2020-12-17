package com.inventory.inventory.ViewModels.Product;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUser;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM{ 
	
	private Boolean all;
	private Boolean employeeIdOrFree;
	
	@DropDownAnnotation(target="name",value="name",name="name",title="select name")
	private List<SelectItem> names;
	private String name;
	
	private Long userId;
	
	@DropDownAnnotation(target="employeeId",value="user.id",name="user.userName",title="select employee")
	private List<SelectItem> employeenames;
	private Long employeeId;
	
	private Boolean freeProducts; 
	
	@DropDownAnnotation(target="inventoryNumber",value="inventoryNumber",name="inventoryNumber",title="select number")
	private List<SelectItem> inventoryNumbers;
	private String inventoryNumber;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;
	private Boolean isDiscarded;
	private Boolean isAvailable;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreatedBefore;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")//
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
	private List<Long> discardedFromServerIds;
	//discardedFromServerIds
	//discardedFromServerIds
	//discardedFromServerIds
	
	/*
	 * public Predicate getFreeProductsPredicate() { return
	 * Expressions.numberTemplate(Long.class,
	 * "COALESCE({0},{1})",QProduct.product.employee.id,0).eq((long) 0); }
	 */
    public Predicate getPredicate() {
		
	 // Predicate freeProductsP = Expressions.numberTemplate(Long.class, "COALESCE({0},{1})",QProduct.product.employee.id,0).eq((long) 0);
        
      LocalDate date = LocalDate.now();
      
	  Predicate predicate = Expressions.asBoolean(true).isTrue();
//			  employeeIdOrFree != null && employeeId !=null && userId != null ? 
//					  (QProduct.product.employee.id.eq(employeeId).or(freeProductsP)).and(QProduct.product.user.id.eq(userId))
//					  :
//			 ( name == null ? Expressions.asBoolean(true).isTrue()
//				  		   : QProduct.product.name.toLowerCase().contains(name.toLowerCase()))
//			  .and( userId == null ? Expressions.asBoolean(true).isTrue()
//					  	    : QProduct.product.user.id.eq(userId)) 
//			  .and( employeeId == null ? Expressions.asBoolean(true).isTrue()
//					  		: QProduct.product.employee.id.eq(employeeId) )					  			 
//			  .and( freeProducts == null ? Expressions.asBoolean(true).isTrue()
//					  : freeProductsP )			 
//			  .and( inventoryNumber == null ? Expressions.asBoolean(true).isTrue()
//					  		:QProduct.product.inventoryNumber.contains(inventoryNumber)) 
//			  .and( productType == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.productType.eq(productType)) 
//			  .and( isDiscarded == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.isDiscarded.eq(isDiscarded)) 
//			  .and( isAvailable == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.isAvailable.eq(isAvailable)) 
//			  .and( dateCreatedBefore == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.dateCreated.before(dateCreatedBefore)) 
//			  .and( dateCreatedAfter == null ? Expressions.asBoolean(true).isTrue() 
//					  		: QProduct.product.dateCreated.after(dateCreatedAfter))
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
//			  .and(amortizationPercentMoreThan ==null ? Expressions.asBoolean(true).isTrue()
//					  : QProduct.product.amortizationPercent.gt(amortizationPercentMoreThan))
//			  .and(amortizationPercentLessThan ==null ? Expressions.asBoolean(true).isTrue()
//					  : QProduct.product.productType.eq(ProductType.DMA).and(
//						  QProduct.product.amortizationPercent.lt(amortizationPercentLessThan)))
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
    	
		Predicate names = Expressions.asBoolean(true).isTrue();
//				userId != null ? 
//				QProduct.product.user.id.eq(userId)
//				: employeeId != null ? 
//			  		 QProduct.product.employee.id.eq( employeeId) 
//			  		 : null ;
		Predicate employeenames = Expressions.asBoolean(true).isTrue();
//				userId != null ? 
//				QUser.user.user_mol.id.eq(userId)	 
//				: null;
					
		dropDownFilters = new HashMap<String, Predicate>() {{		
			  put("names", names);
			  put("employeenames", employeenames);
			  put("inventoryNumbers",names);
		 }};				
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

	public List<Long> getDiscardedFromServerIds() {
		return discardedFromServerIds;
	}

	public void setDiscardedFromServerIds(List<Long> discardedFromServerIds) {
		this.discardedFromServerIds = discardedFromServerIds;
	}

	public Boolean getFreeProducts() {
		return freeProducts;
	}

	public void setFreeProducts(Boolean freeProducts) {
		this.freeProducts = freeProducts;
	}

	public Boolean getAll() {
		return all;
	}

	public void setAll(Boolean all) {
		this.all = all;
	}

	
	public List<SelectItem> getNames() {
		return names;
	}

	public void setNames(List<SelectItem> names) {
		this.names = names;
	}
	
	public List<SelectItem> getEmployeenames() {
		return employeenames;
	}
	
	public void setEmployeenames(List<SelectItem> employeenames) {
		this.employeenames = employeenames;
	}
	
	
	public List<SelectItem> getInventoryNumbers() {
		return inventoryNumbers;
	}
	
	public void setInventoryNumbers(List<SelectItem> inventoryNumbers) {
		this.inventoryNumbers = inventoryNumbers;
	}

	public Boolean getEmployeeIdOrFree() {
		return employeeIdOrFree;
	}

	public void setEmployeeIdOrFree(Boolean employeeIdOrFree) {
		this.employeeIdOrFree = employeeIdOrFree;
	}

	public List<SelectItem> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<SelectItem> productTypes) {
		this.productTypes = productTypes;
	}
	
}


package com.inventory.inventory.ViewModels.Product;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM{ 
	
	private Boolean all;
	
	@DropDownAnnotation(target="name",value="name",name="name",title="select name")
	private List<SelectItem> names;
	private String name;
	
	@JsonIgnore
	private Long userId;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;
	
	private Long totalCountMoreThan;
	private Long totalCountLessThan;
	
	@DropDownAnnotation(target="userCategoryId",value="userCategory.id", name="userCategory.category.name",title="select category", filterBy="productType")
	private List<SelectItem> userCategories;
	private Long userCategoryId;
		
	// for DMA type
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;
	
    public Predicate getPredicate() {
        Predicate predicate = 
			  userId != null ? 
			  (name == null ? Expressions.asBoolean(true).isTrue()
				  		   : QProduct.product.name.toLowerCase().contains(name.toLowerCase()))
			  .and( QProduct.product.userCategory.user.id.eq(userId)) 			  
			  .and( productType == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.userCategory.category.productType.eq(productType)) 			 
			  .and(amortizationPercentMoreThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.userCategory.amortizationPercent.gt(amortizationPercentMoreThan))
			  .and(amortizationPercentLessThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.userCategory.category.productType.eq(ProductType.LTA).and(
						  QProduct.product.userCategory.amortizationPercent.lt(amortizationPercentLessThan)))
			  .and(totalCountMoreThan == null ? Expressions.asBoolean(true).isTrue() :
				  QProduct.product.total.isNotNull().and(QProduct.product.total.gt(totalCountMoreThan)))
			  .and(totalCountLessThan == null ? Expressions.asBoolean(true).isTrue() :
				  QProduct.product.total.isNotNull().and(QProduct.product.total.lt(totalCountLessThan)))
			  .and(userCategoryId == null ? Expressions.asBoolean(true).isTrue() :
				  QProduct.product.userCategory.id.eq(userCategoryId))
			  : null ;
			  
	  return predicate;
    }
    
    @SuppressWarnings("serial")
	@Override
	public void setDropDownFilters() {    	
		Predicate names = 
				userId != null ? 
				QProduct.product.userCategory.user.id.eq(userId)
				:  null ;
				
				Predicate userCategories = 
						userId != null ? 
						QUserCategory.userCategory.user.id.eq(userId)
						:  null ;
					
		dropDownFilters = new HashMap<String, Predicate>() {{		
			  put("names", names);
			  put("userCategories",userCategories);			
		 }};				
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
	public Long getTotalCountMoreThan() {
		return totalCountMoreThan;
	}
	public void setTotalCountMoreThan(Long totalCountMoreThan) {
		this.totalCountMoreThan = totalCountMoreThan;
	}
	public Long getTotalCountLessThan() {
		return totalCountLessThan;
	}
	public void setTotalCountLessThan(Long totalCountLessThan) {
		this.totalCountLessThan = totalCountLessThan;
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
	public List<SelectItem> getUserCategories() {
		return userCategories;
	}
	public void setUserCategories(List<SelectItem> userCategories) {
		this.userCategories = userCategories;
	}
	public Long getUserCategoryId() {
		return userCategoryId;
	}
	public void setUserCategoryId(Long userCategoryId) {
		this.userCategoryId = userCategoryId;
	}

	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {
		// TODO Auto-generated method stub
		return QProduct.product.userCategory.userId.eq(userId).and(QProduct.product.id.eq(id));
	}

	@Override
	public Predicate getListAuthorizationPredicate(List<Long> ids, ERole eRole, Long userId) {
		// TODO Auto-generated method stub
		return QProduct.product.userCategory.userId.eq(userId).and(QProduct.product.id.in(ids));
	}


   
}


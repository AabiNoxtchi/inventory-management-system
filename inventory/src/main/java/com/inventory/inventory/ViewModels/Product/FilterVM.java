package com.inventory.inventory.ViewModels.Product;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.SubCategory;
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
	
	private Integer totalCountMoreThan;
	private Integer totalCountLessThan;
	
	@DropDownAnnotation(target="subCategory",value="subCategory.id",name="subCategory.name",title="select sub-category")
	private List<SelectItem> subCategories;
	private Long subCategoryId;
	
	@DropDownAnnotation(target="Category",value="category.id",name="category.name",title="select category")
	private List<SelectItem> categories;
	private Long categoryId;
	
	
	// for DMA type
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;
	
    public Predicate getPredicate() {
        System.out.println("user id in filter = "+userId);
	  Predicate predicate = 
			  userId != null ? 
			  (name == null ? Expressions.asBoolean(true).isTrue()
				  		   : QProduct.product.name.toLowerCase().contains(name.toLowerCase()))
			  .and( QProduct.product.mol.id.eq(userId)) 			  
			  .and( productType == null ? Expressions.asBoolean(true).isTrue() 
					  		: QProduct.product.productType.eq(productType)) 			 
			  .and(amortizationPercentMoreThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.amortizationPercent.gt(amortizationPercentMoreThan))
			  .and(amortizationPercentLessThan ==null ? Expressions.asBoolean(true).isTrue()
					  : QProduct.product.productType.eq(ProductType.DMA).and(
						  QProduct.product.amortizationPercent.lt(amortizationPercentLessThan)))
			  .and(totalCountMoreThan == null ? Expressions.asBoolean(true).isTrue() :
				  QDeliveryDetail.deliveryDetail.countDistinct().gt(totalCountMoreThan))
			  .and(subCategoryId == null ? Expressions.asBoolean(true).isTrue() :
				  QProduct.product.subCategory.id.eq(subCategoryId))
			  : null ;
			  
	  return predicate;
    }
    
    @SuppressWarnings("serial")
	@Override
	public void setDropDownFilters() {
    	
		Predicate names = 
				userId != null ? 
				QProduct.product.mol.id.eq(userId)
				:  null ;
					
		dropDownFilters = new HashMap<String, Predicate>() {{		
			  put("names", names);
			  put("subCategories", Expressions.asBoolean(true).isTrue());
			  put("categories", Expressions.asBoolean(true).isTrue());
			 
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

	public Integer getTotalCountMoreThan() {
		return totalCountMoreThan;
	}

	public void setTotalCountMoreThan(Integer totalCountMoreThan) {
		this.totalCountMoreThan = totalCountMoreThan;
	}

	public Integer getTotalCountLessThan() {
		return totalCountLessThan;
	}

	public void setTotalCountLessThan(Integer totalCountLessThan) {
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

	public List<SelectItem> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SelectItem> subCategories) {
		this.subCategories = subCategories;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public List<SelectItem> getCategories() {
		return categories;
	}

	public void setCategories(List<SelectItem> categories) {
		this.categories = categories;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	
    
   
}


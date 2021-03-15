package com.inventory.inventory.ViewModels.Product;

import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.SubCategory;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<Product>{
	
	@Nullable
	@JsonIgnore
	private Long userId;

	@NotBlank
	private String name;

	private String description;

	// for DMA type
	private double amortizationPercent;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;
	
	@DropDownAnnotation(target="subCategory",value="subCategory.id",name="subCategory.name",title="select sub-category")
	private List<SubCategory> subCategories;
	private SubCategory subCategory;
	
	@DropDownAnnotation(target="",value="Category.id",name="Category.name",title="select category")
	private List<Category> categories;	

	@Override
	public void populateModel(Product item) {
		
		name = item.getName();
		description = item.getDescription();
		productType=item.getProductType();
		subCategory=item.getSubCategory();
		amortizationPercent= item.getAmortizationPercent();
		
	}

	@Override
	public void populateEntity(Product item) {
		item.setId(getId());		
		item.setUser(userId);		
		item.setName(name);
		item.setDescription(description);				
		item.setProductType(productType);
		item.setSubCategory(subCategory);
		item.setAmortizationPercent(amortizationPercent);		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}

	public List<SelectItem> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<SelectItem> productTypes) {
		this.productTypes = productTypes;
	}

	public List<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	
}

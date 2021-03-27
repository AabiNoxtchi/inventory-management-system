package com.inventory.inventory.ViewModels.Category;

import java.util.HashMap;
import java.util.List;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QCategory;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

//import static com.querydsl.sql.SQLExpressions.select;
//import static com.inventory.inventory.ViewModels.Delivery.SQLExpressions.select;

public class FilterVM extends BaseFilterVM{
	
	
	@DropDownAnnotation(target="name",value="name",name="name",title="select name", filterBy="productType")
	private List<SelectItem> names;
	private String name;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;

	@Override
	public Predicate getPredicate() {

		QCategory category = QCategory.category;

		Predicate p = 
				(name == null ? Expressions.asBoolean(true).isTrue() 
						:category.name.contains(name) )
						.and(productType == null ? Expressions.asBoolean(true).isTrue() 
						: category.productType.eq(productType))												
					  ;	
						
		return p;
	}

	@Override
	public void setDropDownFilters() {
		Predicate p = Expressions.asBoolean(true).isTrue();
		dropDownFilters = new HashMap<String, Predicate>() {
			{
				put("names", p);
				put("productTypes", p);				
			}};
		
	}

	@Override
	public Boolean getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAll(Boolean all) {
		// TODO Auto-generated method stub
		
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

	

}
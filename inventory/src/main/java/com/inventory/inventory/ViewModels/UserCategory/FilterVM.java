package com.inventory.inventory.ViewModels.UserCategory;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QCategory;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;

public class FilterVM extends BaseFilterVM{
	
	@JsonIgnore
	private Long userId;
	
	@DropDownAnnotation(target="categoryId",value="category.id",name="category.name",title="select name", filterBy="category.productType")
	private List<SelectItem> names;
	private Long categoryId;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	private ProductType productType;
	
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;


	@Override
	public Predicate getPredicate() {

		QUserCategory uc = QUserCategory.userCategory;
		Predicate p = 
				(userId!=null?uc.userId.eq(userId):Expressions.asBoolean(true).isFalse())
				.and(
				(categoryId == null ? Expressions.asBoolean(true).isTrue() 
						:uc.category.id.eq(categoryId) )
						.and(productType == null ? Expressions.asBoolean(true).isTrue() 
						: uc.category.productType.eq(productType))
						.and(amortizationPercentMoreThan==null?Expressions.asBoolean(true).isTrue()
								: uc.amortizationPercent.gt(amortizationPercentMoreThan))
						.and(amortizationPercentLessThan==null?Expressions.asBoolean(true).isTrue()
								: uc.amortizationPercent.lt(amortizationPercentLessThan)))
					  ;	
		return p;
	}

	@Override
	public void setDropDownFilters() {
		Predicate p = Expressions.asBoolean(true).isTrue();
		QUserCategory uc = QUserCategory.userCategory;
		Predicate names = userId!=null?QCategory.category.id.in(JPAExpressions.selectFrom(uc).where(uc.userId.eq(userId)).select(uc.category.id)):null;
		dropDownFilters = new HashMap<String, Predicate>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("names", names);
				put("productTypes", p);				
			}};		
	}

	@Override
	public Boolean getAll() {
		return null;
	}

	@Override
	public void setAll(Boolean all) {
	}
	public List<SelectItem> getNames() {
		return names;
	}
	public void setNames(List<SelectItem> names) {
		this.names = names;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
	public void setAmortizationPercentMoreThan(Integer amortizationPercentMoreThan) {
		this.amortizationPercentMoreThan = amortizationPercentMoreThan;
	}
	public Integer getAmortizationPercentLessThan() {
		return amortizationPercentLessThan;
	}
	public void setAmortizationPercentLessThan(Integer amortizationPercentLessThan) {
		this.amortizationPercentLessThan = amortizationPercentLessThan;
	}

	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {
		// TODO Auto-generated method stub
		return QUserCategory.userCategory.id.eq(id).and(QUserCategory.userCategory.userId.eq(userId));
	}

	@Override
	public Predicate getListAuthorizationPredicate(List<Long> ids, ERole role, Long userId) {
		// TODO Auto-generated method stub
		return QUserCategory.userCategory.id.in(ids).and(QUserCategory.userCategory.userId.eq(userId));
	}
	

}


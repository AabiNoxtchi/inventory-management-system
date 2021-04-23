package com.inventory.inventory.ViewModels.UserCategory;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<UserCategory>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@DropDownAnnotation(target="categoryId",value="category.id",name="category.name",title="select name", filterBy="category.productType")
	private List<SelectItem> names;//all minus mol's categories
	private Long categoryId;	
	
	private double amortizationPercent;	
	
	@JsonIgnore
	private Long userId;//from server
	
	@Override
	public void populateModel(UserCategory item) {
		setId(item.getId());
		categoryId = item.getCategory().getId();
		amortizationPercent = item.getAmortizationPercent();		
	}
	
	
	@Override
	public void populateEntity(UserCategory item) {
		item.setId(getId());
		item.setAmortizationPercent(amortizationPercent);
		item.setCategory(new Category(categoryId));
		item.setUser(new MOL(userId));
	
		
	}
	public List<SelectItem> getNames() {
		return names;
	}
	public void setNames(List<SelectItem> names) {
		this.names = names;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public double getAmortizationPercent() {
		return amortizationPercent;
	}
	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	

}
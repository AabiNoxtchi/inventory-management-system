package com.inventory.inventory.ViewModels.UserProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;

public class EditVM extends BaseEditVM<UserProfile>{
	
	private Long previousId;
	
	private Long userId;
	private Long productDetailId;
	private List<Long> productDetailIds;
	
	//private List<Selectable> productDetailSelectables;
//	@JsonIgnore
//	private User user;
//	@JsonIgnore
//	private ProductDetail productDetail;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate givenAt ;//= new Date();
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private LocalDate returnedAt ;//= new Date();

	private List<Long> savedIds;
	//private ECondition conditionReturned;
	
	//private String inventoryNumber;
	

	@Override
	public void populateModel(UserProfile item) {		
		
	}

	@Override
	public void populateEntity(UserProfile item) {
		item.setId(getId());
		item.setUser(new User(userId));
		item.setProductDetail(new ProductDetail(productDetailId));
		item.setGivenAt(givenAt);
		item.setReturnedAt(returnedAt);
		
	}

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public ProductDetail getProductDetail() {
//		return productDetail;
//	}
//
//	public void setProductDetail(ProductDetail productDetail) {
//		this.productDetail = productDetail;
//	}

	

	public Long getProductDetailId() {
		return productDetailId;
	}

	public LocalDate getGivenAt() {
		return givenAt;
	}

	public void setGivenAt(LocalDate givenAt) {
		this.givenAt = givenAt;
	}

	public LocalDate getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(LocalDate returnedAt) {
		this.returnedAt = returnedAt;
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}

	public Long getPreviousId() {
		return previousId;
	}

	public void setPreviousId(Long previousId) {
		this.previousId = previousId;
	}

	public List<Long> getProductDetailIds() {
		return productDetailIds;
	}

	public void setProductDetailIds(List<Long> productDetailIds) {
		this.productDetailIds = productDetailIds;
	}
	public List<Long> getSavedIds() {
		return savedIds;
	}

	public void addToSavedIds(Long id) {
		
		if(savedIds == null)
			savedIds = new ArrayList<>();
		savedIds.add(id);		
	}

	
	
	

//	public List<Selectable> getProductDetailSelectables() {
//		return productDetailSelectables;
//	}
//
//	public void setProductDetailSelectables(List<Selectable> productDetailSelectables) {
//		this.productDetailSelectables = productDetailSelectables;
//	}

	

//	public ECondition getConditionReturned() {
//		return conditionReturned;
//	}
//
//	public void setConditionReturned(ECondition conditionReturned) {
//		this.conditionReturned = conditionReturned;
//	}
	
	
	

}

package com.inventory.inventory.ViewModels.UserProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;

public class EditVM extends BaseEditVM<UserProfile>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long previousId;
	
	private Long userId;
	
	private Long productDetailId; 
	private List<Long> productDetailIds; // save in bulk new profiles for employee

	private LocalDate givenAt;
	
	private LocalDate returnedAt ;//= new Date();

	@JsonIgnore
	private List<Long> savedIds; // if its multi save, track thier number 
		
	private Double paidPlus;
	private ProfileDetail profileDetail;	

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
		item.setProfileDetail(profileDetail);		
	}

	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
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
	public Double getPaidPlus() {
		return paidPlus;
	}
	public void setPaidPlus(Double paidPlus) {
		this.paidPlus = paidPlus;
	}
	public ProfileDetail getProfileDetail() {
		return profileDetail;
	}
	public void setProfileDetail(ProfileDetail profileDetail) {
		this.profileDetail = profileDetail;
	}


}

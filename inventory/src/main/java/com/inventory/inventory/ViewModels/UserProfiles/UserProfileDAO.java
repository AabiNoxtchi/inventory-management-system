package com.inventory.inventory.ViewModels.UserProfiles;

import java.time.LocalDate;

import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.UserProfile;

public class UserProfileDAO {
	
	private Long id;
    private LocalDate givenAt;    
    private LocalDate returnedAt;
   private String userName;
	private Long userId;
	private boolean deletedUser;
    
    private Long productId;
	private String productName;
    private Long productDetailId;
	private String inventoryNumber;
	
	 private ProfileDetail profileDetail;
	 private ECondition condition;
	
	public UserProfileDAO(UserProfile up,
			String userName,			
			String inventoryNumber,
			ECondition condition,
			Long productId,
			String productName	,
			LocalDate deleted){
			
		this.id = up.getId();
		this.givenAt = up.getGivenAt();
		this.returnedAt = up.getReturnedAt();
		this.userName = userName;
		this.userId = up.getUserId();
		this.productId = productId;
		this.productName = productName;
		this.productDetailId = up.getProductDetailId();
		this.inventoryNumber = inventoryNumber;
		this.condition = condition;
		this.profileDetail = up.getProfileDetail();
		this.deletedUser = deleted != null;				
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getProductDetailId() {
		return productDetailId;
	}
	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}
	public String getInventoryNumber() {
		return inventoryNumber;
	}
	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}
	public ProfileDetail getProfileDetail() {
		return profileDetail;
	}
	public void setProfileDetail(ProfileDetail profileDetail) {
		this.profileDetail = profileDetail;
	}
	@Override
	public String toString() {
		return "UserProfileDAO [id=" + id + ", profileDetail " + profileDetail + "]";
	}
	public boolean isDeletedUser() {
		return deletedUser;
	}
	public void setDeletedUser(boolean deletedUser) {
		this.deletedUser = deletedUser;
	}
	public ECondition getCondition() {
		return condition;
	}
	public void setCondition(ECondition condition) {
		this.condition = condition;
	}
	
}



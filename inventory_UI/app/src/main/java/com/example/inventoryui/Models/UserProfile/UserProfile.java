package com.example.inventoryui.Models.UserProfile;

import com.example.inventoryui.Models.Shared.BaseModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class UserProfile extends BaseModel implements Serializable {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date givenAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date returnedAt;
   private String userName;
	private Long userId;
	private boolean deletedUser;
    
    private Long productId;
	private String productName;
    private Long productDetailId;
	private String inventoryNumber;
	
	 private ProfileDetail profileDetail;
	 private ECondition condition;

	public Date getGivenAt() {
		return givenAt;
	}

	public void setGivenAt(Date givenAt) {
		this.givenAt = givenAt;
	}

	public Date getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(Date returnedAt) {
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



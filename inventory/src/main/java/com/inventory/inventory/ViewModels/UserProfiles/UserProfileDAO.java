package com.inventory.inventory.ViewModels.UserProfiles;

import java.time.LocalDate;

import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.UserProfile;

public class UserProfileDAO {
	
	private Long id;
    private LocalDate givenAt;    
    private LocalDate returnedAt;
    private ECondition conditionReturned;
	private String userName;
	private Long userId;
	private boolean deletedUser;
    
    private Long productId;
	private String productName;
    private Long productDetailId;
	private String inventoryNumber;
	
	 private ProfileDetail profileDetail;
	
	public UserProfileDAO(UserProfile up,
		//	ProfileDetail pd,
			String userName,
			
			String inventoryNumber,
			Long productId,
			String productName	,
			LocalDate deleted
			
			){
			
		this.id = up.getId();
		this.givenAt = up.getGivenAt();
		this.returnedAt = up.getReturnedAt();
		//this.conditionReturned = up.getConditionReturned();
		this.userName = userName;
		this.userId = up.getUserId();
		this.productId = productId;
		this.productName = productName;
		this.productDetailId = up.getProductDetailId();
		this.inventoryNumber = inventoryNumber;
		this.profileDetail = up.getProfileDetail();//pd;//up.getProfileDetail();
		this.deletedUser = deleted != null;
				
			}
	/*public UserProfileDAO(Long id, LocalDate givenAt, LocalDate returnedAt,// ECondition conditionReturned,
			String userName, Long userId, Long productId, String productName, Long productDetailId,
			String inventoryNumber) {
		super();
		this.id = id;
		this.givenAt = givenAt;
		this.returnedAt = returnedAt;
		//this.conditionReturned = conditionReturned;
		this.userName = userName;
		this.userId = userId;
		this.productId = productId;
		this.productName = productName;
		this.productDetailId = productDetailId;
		this.inventoryNumber = inventoryNumber;
	}*/
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
	/*public ECondition getConditionReturned() {
		return conditionReturned;
	}
	public void setConditionReturned(ECondition conditionReturned) {
		this.conditionReturned = conditionReturned;
	}*/
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
	
	
}

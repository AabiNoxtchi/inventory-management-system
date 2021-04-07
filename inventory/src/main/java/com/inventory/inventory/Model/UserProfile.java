package com.inventory.inventory.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.querydsl.core.annotations.QueryInit;

@Entity
@Table( name = "user_profile")
public class UserProfile extends BaseEntity implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)   
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private User user;

    @ManyToOne(optional = false)   
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private ProductDetail productDetail;
    
    private LocalDate givenAt;
    
    @Column(nullable = true)
    private LocalDate returnedAt;
    
     @OneToOne( mappedBy="userProfile", cascade = CascadeType.ALL, orphanRemoval = true)	
	 @Basic(fetch = FetchType.LAZY)  // lazy fetched
	 @JsonIgnore  // must otherwise timeline error parsing nulls 
	 private ProfileDetail profileDetail;
       
	public ProfileDetail getProfileDetail() {
		return profileDetail;
	}
	
	public void setProfileDetail(ProfileDetail profileDetail) {
		this.profileDetail = profileDetail;
		profileDetail.setUserProfile(this);
	}

	@Formula("(select user_id)")
	private Long userId;
    
  
    
    @Formula("(select product_detail_id)")
    private Long productDetailId;   
	
    public UserProfile() {}
   
	public UserProfile(Long userId, ProductDetail productDetail,LocalDate givenAt) {
		super();
		this.user = new User(userId);
		this.productDetail = productDetail;
		this.givenAt = givenAt;
	}



	public UserProfile(User user, ProductDetail productDetail, LocalDate givenAt, LocalDate returnedAt) {// ECondition conditionGiving,
			//ECondition conditionReturned) {
		super();
		this.user = user;
		this.productDetail = productDetail;
		this.givenAt = givenAt;
		this.returnedAt = returnedAt;
		//this.conditionGiven = conditionGiving;
		//this.conditionReturned = conditionReturned;
	}
	
	public UserProfile(Long userId, Long productDetailId, LocalDate givenAt, LocalDate returnedAt) {// ECondition conditionGiving,
		//ECondition conditionReturned) {
	super();
	this.user = new User(userId);
	this.productDetail = new ProductDetail(productDetailId);
	this.givenAt = givenAt;
	this.returnedAt = returnedAt;
	//this.conditionGiven = conditionGiving;
	//this.conditionReturned = conditionReturned;
}

	

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ProductDetail getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
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



	//	public ECondition getConditionGiven() {
//		return conditionGiven;
//	}
//	public void setConditionGiven(ECondition conditionGiving) {
//		this.conditionGiven = conditionGiving;
//	}
	/*public ECondition getConditionReturned() {
		return conditionReturned;
	}
	public void setConditionReturned(ECondition conditionReturned) {
		this.conditionReturned = conditionReturned;
	}*/

	/*public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}*/

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductDetailId() {
		return productDetailId;
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}

	@Override
	public String toString() {
		return "UserProfile [ givenAt=" + givenAt
				+ ", returnedAt=" + returnedAt + ", profileDetail=" + profileDetail + ", userId=" + userId
				+ ", productDetailId=" + productDetailId + "]";
	}

	
	
	

//	@Override
//	public String toString() {
//		return "UserProfile [user=" + user + ", productDetail=" + productDetail + ", givenAt=" + givenAt
//				+ ", returnedAt=" + returnedAt + ", profileDetail=" + profileDetail + ", userId=" + userId
//				+ ", productDetailId=" + productDetailId + "]";
//	}




//	@Override
//	public String toString() {
//		return "UserProfile [id = "+getId()+" givenAt=" + givenAt
//				+ ", returnedAt=" + returnedAt + ", conditionReturned=" + conditionReturned + ", userId=" + userId
//				+ ", productDetailId=" + productDetailId + "]";
//	}
//
	
	
}

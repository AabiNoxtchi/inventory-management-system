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
    //@JoinColumn(name = "user_id")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private User user;

    @ManyToOne(optional = false)
    //@JoinColumn(name = "productDetail_id")
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private ProductDetail productDetail;
    
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate givenAt;
    
    @Column(nullable = true)
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnedAt;
    //private ECondition conditionGiven;
    
   /* @Column(nullable = true) 
    private ECondition conditionReturned;*/
    
     @OneToOne( mappedBy="userProfile", cascade = CascadeType.ALL)//, orphanRemoval = true)	// to see that it's been damaged
	 @Basic(fetch = FetchType.LAZY)  // lazy fetched
	 @JsonIgnore  // must otherwise timeline error parsing nulls 
	 private ProfileDetail profileDetail;
     
    
    
    //@Formula("(select u.user_name from user u where u.id = user_id)")
	//private String userName;
    
   
	public ProfileDetail getProfileDetail() {
		return profileDetail;
	}
	
	public void setProfileDetail(ProfileDetail profileDetail) {
		this.profileDetail = profileDetail;
		profileDetail.setUserProfile(this);
	}

	@Formula("(select user_id)")
	private Long userId;
    
   /* @Formula("(select p.id from "
    		+ "product p inner join "
    		+ "delivery_detail dd on p.id = dd.product_id inner join "
    		+ "product_detail pd on pd.delivery_detail_id = dd.id "    		
    		+ "where pd.id = product_detail_id)")
    private Long productId;
    
    @Formula("(select p.name from "
    		+ "product p inner join "
    		+ "delivery_detail dd on p.id = dd.product_id inner join "
    		+ "product_detail pd on pd.delivery_detail_id = dd.id "    		
    		+ "where pd.id = product_detail_id)")
	private String productName;*/
    
    @Formula("(select product_detail_id)")
    private Long productDetailId;
    
   // @Formula("(select pd.inventory_number from product_detail pd where pd.id = product_detail_id)")
	//private String inventoryNumber;
	
    public UserProfile() {}
    
    
    
	/*public UserProfile(User user, LocalDate givenAt) {
		super();
		this.user = user;
		this.givenAt = givenAt;
	}*/
	
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

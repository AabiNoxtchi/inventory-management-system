package com.inventory.inventory.Model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.User;

@Entity
@Table( name = "user_profile")
public class UserProfile extends BaseEntity implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private LocalDate givenAt;
	    
	 @Column(nullable = true)
	 private LocalDate returnedAt;

	@ManyToOne(optional = false)   
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private User user;

    @ManyToOne(optional = false)   
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private ProductDetail productDetail;
    
     @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)	
	 @Basic(fetch = FetchType.LAZY) 
	 @JsonIgnore  // must otherwise timeline error parsing nulls 
	 private ProfileDetail profileDetail;

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

	public UserProfile(User user, ProductDetail productDetail, LocalDate givenAt, LocalDate returnedAt) {
		super();
		this.user = user;
		this.productDetail = productDetail;
		this.givenAt = givenAt;
		this.returnedAt = returnedAt;		
	}
	
	public UserProfile(Long userId, Long productDetailId, LocalDate givenAt, LocalDate returnedAt) {
		super();
		this.user = new User(userId);
		this.productDetail = new ProductDetail(productDetailId);
		this.givenAt = givenAt;
		this.returnedAt = returnedAt;
	}

	public ProfileDetail getProfileDetail() {
		return profileDetail;
	}
	
	public void setProfileDetail(ProfileDetail profileDetail) {
		this.profileDetail = profileDetail;
		if(profileDetail != null) profileDetail.setUserProfile(this);
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setUserId(Long id) {
		this.user = new User(id);
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
	public Long getUserId() {
		return userId;
	}
	public Long getProductDetailId() {
		return productDetailId;
	}
	
}



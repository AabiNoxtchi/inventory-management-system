package com.inventory.inventory.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "profileDetail")
public class ProfileDetail {
	
	
	@Id
    private Long id; 
    
 
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private UserProfile userProfile;

    private LocalDate createdAt;
    private LocalDate modifiedAt;
    
    @DecimalMin(value = "0.0", inclusive = false)
	@Column(nullable= false, precision=9, scale=2) 
    private BigDecimal owedAmount;
    
    @DecimalMin(value = "0")//, inclusive = false)
	@Column(nullable= false, precision=9, scale=2) 
   // @ColumnDefault("0")
    private BigDecimal paidAmount ;//=  new BigDecimal("0");
    
    private boolean cleared = false;

	public ProfileDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


	public ProfileDetail(LocalDate createdAt, @DecimalMin(value = "0.0", inclusive = false) BigDecimal owedAmount,
			@DecimalMin(value = "0.0") BigDecimal paidAmount) {
		super();
		this.createdAt = createdAt;
		this.owedAmount = owedAmount;
		this.paidAmount = paidAmount;
	}




	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	

	public UserProfile getUserProfile() {
		return userProfile;
	}


	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}


	public LocalDate getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}


	public LocalDate getModifiedAt() {
		return modifiedAt;
	}


	public void setModifiedAt(LocalDate modifiedAt) {
		this.modifiedAt = modifiedAt;
	}


	public BigDecimal getOwedAmount() {
		return owedAmount;
	}


	public void setOwedAmount(BigDecimal owedAmount) {
		this.owedAmount = owedAmount;
	}


	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	
	


	public boolean isCleared() {
		return cleared;
	}




	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}




	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}




//	@Override
//	public String toString() {
//		return "ProfileDetail [id=" + id + ", createdAt=" + createdAt + ", modifiedAt="
//				+ modifiedAt + ", owedAmount=" + owedAmount + ", paidAmount=" + paidAmount + "]";
//	}
	
	
    
    

}

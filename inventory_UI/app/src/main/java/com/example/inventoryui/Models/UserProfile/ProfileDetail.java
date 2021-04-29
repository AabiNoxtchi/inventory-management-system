package com.example.inventoryui.Models.UserProfile;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;


public class ProfileDetail {

    private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date modifiedAt;

    private BigDecimal owedAmount;

    private BigDecimal paidAmount;
    
    private boolean cleared;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
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
	
}



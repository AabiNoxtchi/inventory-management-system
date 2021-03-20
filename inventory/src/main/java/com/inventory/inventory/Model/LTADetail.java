package com.inventory.inventory.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ltaDetail")
public class LTADetail {
	
	@Id
    private Long id; 
    
 
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserCategory userCategory;
    
    private double amortizationPercent;
    
    
    
    public LTADetail(UserCategory userCategory, double amortizationPercent) {
		super();
		this.userCategory = userCategory;
		this.amortizationPercent = amortizationPercent;
	}

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}

}

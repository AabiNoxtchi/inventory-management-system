package com.inventory.inventory.Model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "category",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "number")
		})
public class Category extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;// category 1,2,.... 7		
	private int amortizationPercent;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getAmortizationPercent() {
		return amortizationPercent;
	}
	public void setAmortizationPercent(int amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}
	

}

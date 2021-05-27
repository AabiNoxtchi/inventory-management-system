package com.inventory.inventory.ViewModels.Supplier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.EmailAnnotation;
import com.inventory.inventory.Annotations.PhoneNumberAnnotation;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;

public class EditVM extends BaseEditVM<Supplier>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size(min = 5)
	@Size(max = 150)
    private String name; 	  
	
	@PhoneNumberAnnotation
    private String phoneNumber;
    
	@NotBlank
    @Size(min=4, max=15, message="DDC number not valid")
    private String DDCnumber;	 
   
    @Size(max = 150)
    @EmailAnnotation
    private String email;
    
    @JsonIgnore
    private User mol;

	@Override
	public void populateModel(Supplier item) {
		
		setId(item.getId());
		name = item.getName();
		phoneNumber=item.getPhoneNumber();
		DDCnumber=item.getDDCnumber();
		email = item.getEmail();	
	}

	@Override
	public void populateEntity(Supplier item) {
		System.out.println("ddc = "+DDCnumber);
		item.setId(getId());
		item.setName(name);
		item.setPhoneNumber(phoneNumber);
		item.setDDCnumber(DDCnumber);
		item.setEmail(email);
		item.setUser(mol.getId());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDDCnumber() {
		return DDCnumber;
	}
	public void setDDCnumber(String dDCnumber) {
		DDCnumber = dDCnumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public User getMol() {
		return mol;
	}
	public void setMol(User mol) {
		this.mol = mol;
	}	
	public void setMol(Long molId) {
		this.mol = new User(molId);
	}


}

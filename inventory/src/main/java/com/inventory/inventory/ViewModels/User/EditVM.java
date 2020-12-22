package com.inventory.inventory.ViewModels.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.auth.Models.RegisterRequest;

public class EditVM extends BaseEditVM<User>{
	
	@Size(max = 50)
    private String firstName; 	  
	
	@Size(max = 50)
    private String lastName;	
    
	@NotBlank
    @Size(min = 3, max=150)
    private String userName;	 
   
    @Size(max = 150)
    @Email
    private String email;
    
    private Role role;
    
    private String password;
    

	@Override
	public void PopulateModel(User item) {
		
		setId(item.getId());
		firstName = item.getFirstName();
		lastName=item.getLastName();
		userName=item.getUserName();
		email = item.getEmail();
		
		
	}

	@Override
	public void PopulateEntity(User item) {
		
//		item.setId(getId());
//		item.setFirstName(firstName);
//		item.setLastName(lastName);
//		item.setEmail(email);
	}
	
	public RegisterRequest registerRequest() {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setId(getId());
		registerRequest.setFirstName(firstName);
		registerRequest.setLastName(lastName);
		registerRequest.setUsername(userName);
		registerRequest.setEmail(email);
		registerRequest.setPassword(password);
		registerRequest.setRole(role);
		return registerRequest;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
  
   
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRole() {
      return this.role;
    }
    
    public void setRole(Role role) {
      this.role = role;
    }
	    

}

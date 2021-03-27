package com.inventory.inventory.auth.Models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User.User;


public class RegisterRequest {
	
	    private Long id;

		@Size(max = 50)
	    private String firstName; 	  
		
		@Size(max = 50)
	    private String lastName;	
	    
		@NotBlank
	    @Size(min = 3, max=150)
	    private String username;	 
	   
	    @Size(max = 150)
	    @Email
	    private String email;
	    
	   // private ERole role;
	    
	    private Long cityId;
	    
	   // @NotBlank
	   // @Size(min = 6, max=150)
	    private String password;
	    
	    public void populateEntity(User item, ERole role) {
//	    	new User(registerRequest.getFirstName(),
//					registerRequest.getLastName(), registerRequest.getUsername(), 					 
//					registerRequest.getPassword(), registerRequest.getEmail(),role);
//	    	
	    	item.setFirstName(firstName);
	    	item.setLastName(lastName);
	    	item.setUserName(username);
	    	item.setPassword(password);
	    	item.setEmail(email);
	    	item.setErole(role);
	    }
	    
	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
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
	  
	    public String getUsername() {
	        return username;
	    }
	 
	    public void setUsername(String username) {
	        this.username = username;
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
	    
//	    public ERole getRole() {
//	      return this.role;
//	    }
//	    
//	    public void setRole(ERole role) {
//	      this.role = role;
//	    }

		public Long getCityId() {
			return cityId;
		}

		public void setCityId(Long cityId) {
			this.cityId = cityId;
		}
	    
	    

}

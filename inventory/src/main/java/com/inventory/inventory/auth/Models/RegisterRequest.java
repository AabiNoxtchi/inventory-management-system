package com.inventory.inventory.auth.Models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;

@Entity
@Table(name = "pendingUser")
public class RegisterRequest extends BaseEntity implements Serializable{
	
	
//	@javax.persistence.Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;

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
	    
	    private String newCity;
	    private Long countryId;
	    
	    public void populateEntity(User item, ERole role) {
//	    	new User(registerRequest.getFirstName(),
//					registerRequest.getLastName(), registerRequest.getUsername(), 					 
//					registerRequest.getPassword(), registerRequest.getEmail(),role);
//	    	
	    	item.setId(getId());
	    	item.setFirstName(firstName);
	    	item.setLastName(lastName);
	    	item.setUserName(username);
	    	item.setPassword(password);
	    	item.setEmail(email);
	    	item.setErole(role);
	    }
	    
	    public Employee getEmployee(ERole role, Long molId) {
	    	
	    	Employee employee = new Employee();
	    	employee.setId(getId());
	    	employee.setFirstName(firstName);
	    	employee.setLastName(lastName);
	    	employee.setUserName(username);
	    	employee.setEmail(email);
	    	employee.setPassword(password);
	    	employee.setErole(role);
	    	employee.setMol(molId);
	    	return employee;
	    	
	    	
	    }
	    
	    public MOL getMol(ERole role, LocalDate lastActive) {
	    	
	    	MOL mol = new MOL();
	    	mol.setId(getId());
	    	mol.setFirstName(firstName);
	    	mol.setLastName(lastName);
	    	mol.setUserName(username);
	    	mol.setEmail(email);
	    	mol.setPassword(password);
	    	mol.setErole(role);
	    	mol.setCity(new City(cityId));
	    	mol.setLastActive(lastActive);
	    	//employee.setMol(molId);
	    	return mol;
	    	
	    	
	    }
	    
	    
	    
//	    public Long getId() {
//			return id;
//		}
//
//		public void setId(Long id) {
//			this.id = id;
//		}
	    
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

		public String getNewCity() {
			return newCity;
		}

		public void setNewCity(String newCity) {
			this.newCity = newCity;
		}

		public Long getCountryId() {
			return countryId;
		}

		public void setCountryId(Long countryId) {
			this.countryId = countryId;
		}

		@Override
		public String toString() {
			return "RegisterRequest [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
					+ ", email=" + email + ", cityId=" + cityId + ", password=" + password + ", newCity=" + newCity
					+ ", countryId=" + countryId + "]";
		}
	    
	    

}

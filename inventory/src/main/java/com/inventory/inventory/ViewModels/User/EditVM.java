package com.inventory.inventory.ViewModels.User;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Annotations.EmailAnnotation;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.RegisterRequest;

@JsonInclude(Include.NON_NULL)
public class EditVM extends BaseEditVM<User>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size(max = 50)
    private String firstName; 	  
	
	@Size(max = 50)
    private String lastName;	
    
	@NotBlank
    @Size(min = 3, max=150)
    private String userName;	 
   
    @Size(max = 150)
    @EmailAnnotation
    private String email;    
  
    private String password;
    
    @DropDownAnnotation(target="cityId",value="city.id", name="city.name",title="select city", filterBy="countryId")
	private List<SelectItem> cities;
	private Long cityId;
	
	@DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
	private List<SelectItem> countries;
	private Long countryId;    

	@Override
	public void populateModel(User item) {
		
		setId(item.getId());
		firstName = item.getFirstName();
		lastName=item.getLastName();
		userName=item.getUserName();
		email = item.getEmail();
	}

	@Override
	public void populateEntity(User item) {	
	}
	
	public RegisterRequest registerRequest() {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setId(getId());
		registerRequest.setFirstName(firstName);
		registerRequest.setLastName(lastName);
		registerRequest.setUsername(userName);
		registerRequest.setEmail(email);
		registerRequest.setPassword(password);
		registerRequest.setCityId(cityId);
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
	public List<SelectItem> getCities() {
		return cities;
	}
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public List<SelectItem> getCountries() {
		return countries;
	}
	public void setCountries(List<SelectItem> countries) {
		this.countries = countries;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	    

}

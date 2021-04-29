package com.example.inventoryui.Models.User;

import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User extends BaseModel implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private boolean deleted;

    private String countryName;
    private String cityName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date lastActive;

    private Role role;
    private List<SelectItem> cities;
    private Long cityId;

    private List<SelectItem> countries;
    private Long countryId;

    public User() {}

    public User(Long id,String userName,String role){
        setId(id);
        setUserName(userName);
        setRole(Role.valueOf(role));
    }

    public User(String userName, String password,
                String email, Role role) {
       // super();
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
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

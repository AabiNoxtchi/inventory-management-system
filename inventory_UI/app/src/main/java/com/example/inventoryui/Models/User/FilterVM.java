package com.example.inventoryui.Models.User;

import com.example.inventoryui.Annotations.CheckBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterVM extends BaseFilterVM implements Serializable {


    @CheckBoxAnnotation(title="all", target = "all")
    protected Boolean all;

    @DropDownAnnotation(target="firstName",value="firstName",name="firstName",title="first name")
    private List<SelectItem> firstNames;
    @SkipAnnotation
    protected String firstName;

    @DropDownAnnotation(target="lastName",value="lastName",name="lastName",title="last name")
    private List<SelectItem> lastNames;
    @SkipAnnotation
    protected String lastName;

    @DropDownAnnotation(target="userName",value="userName",name="userName",title="user name")
    private List<SelectItem> userNames;
    @SkipAnnotation
    protected String userName;

    @DropDownAnnotation(target="email",value="email",name="email",title="email")
    private List<SelectItem> emails;
    @SkipAnnotation
    protected String email;

    @DropDownAnnotation(target="countryId",value="country.id", name="country.name",title="select country")
    private List<SelectItem> countries;//existing in data base
    private Long countryId;

    @DropDownAnnotation(target="cityId",value="city.id", name="city.name",title="select city", filterBy="countryId")
    private List<SelectItem> cities;
    private Long cityId;

    //@DateTimeFormat(iso = ISO.DATE)
    //private LocalDate lastActiveBefore;

    @DateAnnotation(target="lastActivebefore",title="last Active Before")
   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonAlias({"lastActiveBefore"})
    private Date lastActivebefore;

    @SkipAnnotation
    private Map<String,Object> urlParameters;


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

    public List<SelectItem> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<SelectItem> userNames) {
        this.userNames = userNames;
    }

    public Boolean getAll() {
        return all;
    }

    public void setAll(Boolean all) {
        this.all = all;
    }

    @JsonIgnore
    public Map<String, Object> getUrlParameters() {
        if(urlParameters==null)
            urlParameters = new HashMap<>();
        return urlParameters;
    }

    @JsonIgnore
    public void setUrlParameters(Map<String, Object> urlParameters) {
        this.urlParameters = urlParameters;
    }

    public List<SelectItem> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(List<SelectItem> firstNames) {
        this.firstNames = firstNames;
    }

    public List<SelectItem> getLastNames() {
        return lastNames;
    }

    public void setLastNames(List<SelectItem> lastNames) {
        this.lastNames = lastNames;
    }

    public List<SelectItem> getEmails() {
        return emails;
    }

    public void setEmails(List<SelectItem> emails) {
        this.emails = emails;
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

   // @JsonProperty("lastActiveBefore")
    public Date getLastActivebefore() {
        return lastActivebefore;
    }

   /* public void setLastActivebefore(Date lastActivebefore) {
        this.lastActivebefore = lastActivebefore;
    }*/
}

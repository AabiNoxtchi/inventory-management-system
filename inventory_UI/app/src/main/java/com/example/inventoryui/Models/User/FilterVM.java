package com.example.inventoryui.Models.User;

import com.example.inventoryui.Annotations.CheckBoxAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
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
}

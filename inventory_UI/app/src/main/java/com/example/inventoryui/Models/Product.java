package com.example.inventoryui.Models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {

    private Long id;

    private String name;

    private User user;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    private Employee employee;

    private String inventoryNumber;

    private String description;

    private ProductType productType;

    private int yearsToDiscard;

    private boolean isDiscarded;

    private boolean isAvailable;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date dateCreated=new Date();

    //for DMA type
    private Integer amortizationPercent;

    //for DMA type
    private Integer yearsToMAConvertion;

    public Product(Long id, String name, User user, String inventoryNumber,
                   String description, ProductType productType,
                   int yearsToDiscard, boolean isDiscarded,
                   boolean isAvailable, Date dateCreated,
                   Integer amortizationPercent, Integer yearsToMAConvertion) {
        this.id = id;
        this.user = user;

        this.name=name;
        this.inventoryNumber = inventoryNumber;
        this.productType = productType;
        this.dateCreated = dateCreated;
        this.description = description;

        this.isAvailable = isAvailable;
        this.isDiscarded = isDiscarded;

        this.yearsToDiscard = yearsToDiscard;

        this.amortizationPercent = amortizationPercent;
        this.yearsToMAConvertion = yearsToMAConvertion;
    }

    public Product(String name){
        this.name=name;
    }
    public Product(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public int getYearsToDiscard() {
        return yearsToDiscard;
    }

    public void setYearsToDiscard(int yearsToDiscard) {
        this.yearsToDiscard = yearsToDiscard;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    public void setDiscarded(boolean isDiscarded) {
        this.isDiscarded = isDiscarded;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    public Date getDateCreated() {
        return dateCreated;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getAmortizationPercent() {
        return amortizationPercent;
    }

    public void setAmortizationPercent(Integer amortizationPercent) {
        this.amortizationPercent = amortizationPercent;
    }

    public Integer getYearsToMAConvertion() {
        return yearsToMAConvertion;
    }

    public void setYearsToMAConvertion(Integer yearsToMAConvertion) {
        this.yearsToMAConvertion = yearsToMAConvertion;
    }

    @Override
    public String toString(){
        return this.getName();

    }



    @Override
    public boolean equals(Object o) {
        if(this==o)
            return true;
        if(o==null)
            return false;
        Product product=(Product)o;
        return
                getName().equals(product.getName());//&& getStatus()!=null?getStatus().equals(user.getStatus()):true;

    }

    @Override
    public int hashCode() {

        int result=1;
        result*=(getName().hashCode());
        return result;
    }

}



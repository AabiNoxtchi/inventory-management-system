package com.example.inventoryui.Models.Product;

import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Models.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Product extends BaseModel implements Serializable {



    private String name;
    private User user;
   // private Employee employee;
    private Long  employee_id;
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

    public Product(){}

    public Product(Long id){
        super(id);
    }


    public Product(String name){
            this.name=name;
    }


    public Product(Long id, String name, User user, String inventoryNumber,
                   String description, ProductType productType,
                   int yearsToDiscard, boolean isDiscarded,
                   boolean isAvailable, Date dateCreated,
                   Integer amortizationPercent, Integer yearsToMAConvertion) {
        super.setId(id);
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

    public Long getEmployee_id(){
        return employee_id;
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
        if(o == null) {
            return false;
        } else if (!(o instanceof Product)) {
            return false;
        } else {
            return ((Product) o).getName().equals(this.getName()) ;
        }
    }

    public boolean equalsByStringId(String Id){
        if(super.getId().toString().equals(Id))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result=5;
        result*=(this.getId().hashCode());
        return result;
    }
}



package com.example.inventoryui.Models.Product;

import com.example.inventoryui.Models.Shared.BaseOrderBy;

import java.io.Serializable;


public class OrderBy extends BaseOrderBy implements Serializable {
    public OrderBy() {
    }

    private String name;

    private String inventoryNumber;

    private ProductType productType;

    private int yearsToDiscard;

    private boolean isDiscarded;

    private boolean isAvailable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
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

}

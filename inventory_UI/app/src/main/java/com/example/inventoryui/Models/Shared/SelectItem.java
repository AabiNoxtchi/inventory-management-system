package com.example.inventoryui.Models.Shared;

public class SelectItem {

    private String value;
    private String name;

    public SelectItem(){}
    public SelectItem(String name){
        this.name=name;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){

        return this.name;
    }
}

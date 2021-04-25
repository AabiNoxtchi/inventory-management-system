package com.example.inventoryui.Models.Shared;

public class SelectItem {

    private String value;
    private String name;
    private String filterBy;

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

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    @Override
    public String toString(){

        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof SelectItem)) {
            return false;
        } else {
            return ((SelectItem) o).getName().equals(this.getName()) ;
        }
    }


    @Override
    public int hashCode() {
        int result=5;
        result*=(this.getName().hashCode());
        return result;
    }
}

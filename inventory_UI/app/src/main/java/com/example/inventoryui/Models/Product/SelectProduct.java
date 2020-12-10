package com.example.inventoryui.Models.Product;

public class SelectProduct {

    private String name;
    private Long totalCount;
    private int count;

    public SelectProduct(){}
    public SelectProduct(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

   @Override
   public String toString(){
        return getName();
   }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof SelectProduct)) {
            return false;
        } else {
            return ((SelectProduct) o).getName().equals(this.getName()) ;
        }
    }


    @Override
    public int hashCode() {
        int result=5;
        result*=(this.getName().hashCode());
        return result;
    }
}

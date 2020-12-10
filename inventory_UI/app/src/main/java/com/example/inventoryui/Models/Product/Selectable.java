package com.example.inventoryui.Models.Product;

import java.util.List;

public class Selectable {

    private Long count;
    private List<SelectProduct> selectProducts;

    private Long empId;

    public Selectable() {
        super();
    }
    public Selectable(Long count, List<SelectProduct> selectProducts) {
        super();
        this.count = count;
        this.selectProducts = selectProducts;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public List<SelectProduct> getSelectProducts() {
        return selectProducts;
    }
    public void setSelectProducts(List<SelectProduct> selectProducts) {
        this.selectProducts = selectProducts;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }
}

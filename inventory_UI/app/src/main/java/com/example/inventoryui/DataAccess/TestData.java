package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;

public class TestData extends BaseData<com.example.inventoryui.Models.Product.IndexVM> {

    public TestData(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Class getIndexVMClass() {
        return com.example.inventoryui.Models.Product.IndexVM.class;
    }

    @Override
    String addToUrl() {
        return "/products" ;
    }
}

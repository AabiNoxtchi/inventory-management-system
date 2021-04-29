package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.inventoryui.Models.Inventory.Inventory;

public class InventoriesData extends BaseData<Inventory, com.example.inventoryui.Models.Inventory.IndexVM> {

    public InventoriesData(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Class getIndexVMClass() {
        return com.example.inventoryui.Models.Inventory.IndexVM.class;
    }

    @Override
    protected Class EClass() {
        return Inventory.class;
    }

    @Override
    String addToUrl() {
            return "/productdetails";
    }
}

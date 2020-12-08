package com.example.inventoryui.Activities.Products;

public class selectDialog {

    final String TAG = "selectDialog";

    private static selectDialog instance;
    private selectDialog() {}
    public synchronized static selectDialog getInstance() {
        if (instance == null) {
            instance = new selectDialog();
        }
        return instance;
    }



}

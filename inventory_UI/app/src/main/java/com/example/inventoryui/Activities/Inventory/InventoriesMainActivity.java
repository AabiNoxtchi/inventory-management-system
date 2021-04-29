package com.example.inventoryui.Activities.Inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.ComparableInputs;
import com.example.inventoryui.DataAccess.InventoriesData;
import com.example.inventoryui.Models.Inventory.FilterVM;
import com.example.inventoryui.Models.Inventory.IndexVM;
import com.example.inventoryui.Models.Inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoriesMainActivity extends BaseMainActivity<Inventory, IndexVM, FilterVM, InventoriesData> {

    static String event1; static String newMsg1; static String message1;

    String amortizedIdsFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected IndexVM getNewIndexVM() {
        return new IndexVM();
    }

    @Override
    protected FilterVM getNewFilter() {
        return new FilterVM();
    }

    @Override
    protected InventoriesData getItemData() {
        return new ViewModelProvider(this).get(InventoriesData.class);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new InventoriesAdapter(this, super.items, new InventoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Inventory item, int position) {

                if (((InventoriesAdapter) adapter).multiSelect) {

                    ifAdapterMultiSelect(item, position );

                } else {
                   // Intent i = new Intent(InventoriesMainActivity.this, ProductAddActivity.class);
                   // i.putExtra("productForUpdate", item);
                    //startActivity(i);
                }
            }
        }, new InventoriesAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Inventory item, int position) {

                onLongClick(item,position);
            }
        }, getActionMode());
    }

    private void addFabOnClick() {
    }

    @Override
    protected void checkAddFabForLoggedUser() {
    }

    @Override
    protected void checkItemsFromIntent() {

        Intent i = getIntent();
        if(i.hasExtra("amortized")) {
            amortizedIdsFromIntent = i.getStringExtra("amortized");
        }

        if (amortizedIdsFromIntent != null && amortizedIdsFromIntent.length() > 1) {

            model.setFilter(getNewFilter());
            model.getFilter().getUrlParameters().put("ids", getList(amortizedIdsFromIntent));
            amortizedIdsFromIntent = null;

            specialFilters = "fully amortized";
            setSecondFilterLayout(specialFilters);
        }
    }

    @Override
    protected void setAdapterMultiSelectFalse() {
        ((InventoriesAdapter)adapter).setMultiSelect(false);
    }

    protected boolean arrangeFilterComparableLayouts(List<ComparableInputs> inputs, LinearLayout filterLayout){
        addDateComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName()
                        .contains("date created")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName()
                        .contains("price")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName()
                        .contains("amortization percent")).findAny().orElse(null));
        return true;
    }

    protected void handleMsg() {
        showDialogAlert(event1, newMsg1, message1);
    }

    public void updateUifromThread(final String event, final String newMsg, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               showDialogAlert(event, newMsg, message);
            }
        });
    }

    public void refresh(final String event, final String newMsg, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(InventoriesMainActivity.this,
                        "refreshing new updates ",Toast.LENGTH_LONG).show();
                getItems();
            }
        });
    }

    public static void takeEventMsg(final String event, final String newMsg, final String message){

        event1 = event;
        newMsg1 = newMsg;
        message1 = message;
        eventMsg = true;
    }

    private void showDialogAlert(final String event, final String newMsg, final String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InventoriesMainActivity.this)
                .setTitle(event).setMessage(newMsg+"\n\n"+"show "+event+" inventories separately ?")
               .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        amortizedIdsFromIntent =  message;
                        getItems();
                    }
                }).setNegativeButton("keep here", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private List getList(String idsString) {
        List<Long> ids = new ArrayList<>();
        idsString = idsString.substring(1, idsString.length() - 1);
        List<String> idsStringList = new ArrayList<>(Arrays.asList(idsString.split(",")));
        for( String id : idsStringList){
            ids.add(Long.valueOf(id));
        }
        return ids;
    }

}


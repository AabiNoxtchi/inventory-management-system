package com.example.inventoryui.Activities.Products;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.ComparableInputs;
import com.example.inventoryui.Activities.User.UsersMainActivity;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProductsMainActivity extends BaseMainActivity<Product,IndexVM,FilterVM, ProductsData> {

    static String TAG = "productsMain";

    String discardedProductsIdsFromIntent = "discardedProductsIds";
    String productsIdsFromIntentList;
    static String event1; static String newMsg1; static String message1;


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
    protected ProductsData getItemData() {
        return new ViewModelProvider(this).get(ProductsData.class);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsAdapter(this, super.items, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item, int position) {

                if (((ProductsAdapter) adapter).multiSelect) {

                    ifAdapterMultiSelect(item, position );

                } else {
                    Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                    i.putExtra("productForUpdate", item);
                    startActivity(i);
                }
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item, int position) {

                onLongClick(item,position);

            }
        }, getActionMode());
    }

    @Override
    protected void checkAddFabForLoggedUser() {
        if(loggedUser.getRole().equals(Role.ROLE_Mol))
        {
            addFab=findViewById(R.id.addFab);
            addFab.show();
            addFabOnClick();
        }
    }

    @Override
    protected void checkItemsFromIntent() {

        Intent i = getIntent();
        if(i.hasExtra(discardedProductsIdsFromIntent)) {
            productsIdsFromIntentList = i.getStringExtra(discardedProductsIdsFromIntent);
        }

        Log.i(TAG, "productsIdsFromIntentList = "+productsIdsFromIntentList);

        if (productsIdsFromIntentList != null && productsIdsFromIntentList.length() > 1) {

            model.setFilter(getNewFilter());
            model.getFilter().getUrlParameters().put("discardedFromServerIds", getList(productsIdsFromIntentList));
            productsIdsFromIntentList = null;

            specialFilters = "auto discarded";
            setSecondFilterLayout(specialFilters);
        }
    }

    @Override
    protected void setAdapterMultiSelectFalse() {
        ((ProductsAdapter)adapter).setMultiSelect(false);
    }

    protected boolean arrangeFilterComparableLayouts(List<ComparableInputs> inputs, LinearLayout filterLayout){
        addDateComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName().contains("date created")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName().contains("discard years total")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName().contains("discard years left")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName().contains("amortization percent")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName().contains("MA conversion years total")).findAny().orElse(null));
        addComparableLayout(filterLayout,
                inputs.stream().filter(c->c.getName().contains("MA conversion years left")).findAny().orElse(null));
        return true;
    }

    public void updateUifromThread(final String event, final String newMsg, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

               showDialogAlert(event, newMsg, message);
            }
        });
    }

    public static void takeEventMsg(final String event, final String newMsg, final String message){

        Log.i(TAG,"take Event msg got msg ");
                event1 = event;
                newMsg1 = newMsg;
                message1 = message;
                eventMsg = true;
    }

    protected void handleMsg() {
        Log.i(TAG,"in  handle msg ");
        showDialogAlert(event1, newMsg1, message1);
    }

    private void showDialogAlert(final String event, final String newMsg, final String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductsMainActivity.this)
                .setTitle(event).setMessage(newMsg+"\n\n"+"show discarded products separately ?")
                //.setCancelable(false)
                .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        productsIdsFromIntentList =  message;
                        getItems();
                    }
                }).setNegativeButton("keep here", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<Long> ids = getList( message );
                for(Product product : items){
                    Log.i(TAG," productsId = "+product.getId());
                    if(ids.size() < 1) break;
                    for(Long discardedProductId : ids){

                        Log.i(TAG," discardedid = "+discardedProductId);
                        Log.i(TAG," (product.getId() == discardedProductId) = "+(product.getId() == discardedProductId));
                        Log.i(TAG," Objects.equals(product.getId(),discardedProductId) = "+ Objects.equals(product.getId(),discardedProductId));

                        if(Objects.equals(product.getId(),discardedProductId))
                        {
                            ids.remove(discardedProductId);
                            product.setDiscarded(true);
                            Log.i(TAG," indexof(product = "+items.indexOf(product));
                            adapter.notifyItemChanged(items.indexOf(product));
                            break;
                        }
                    }
                }

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products_main_menu,menu);

        if(loggedUser.getRole().equals(Role.ROLE_Employee)){
            menu.findItem(R.id.employees).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                logOut();
                return true;
            case R.id.employees:
                //to employees
                Intent toEmployees=new Intent(ProductsMainActivity.this, UsersMainActivity.class);
                startActivity(toEmployees);
                return true;

            case R.id.filter_icon:

                filterActivity();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addFabOnClick() {
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                startActivity(i);
            }
        });
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


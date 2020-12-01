package com.example.inventoryui.Activities.Products;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Employees.EmployeesMainActivity;
import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.ComparableInputs;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductsMainActivity extends BaseMainActivity<Product,IndexVM,FilterVM, ProductsData> {

    String discardedProductsIdsFromIntent = "discardedProductsIds";
    String productsIdsFromIntentList;

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
        return  new ViewModelProvider(this).get(ProductsData.class);
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
    protected void checkIntentAndGetItems() {
        Intent i = getIntent();
        if(i.hasExtra(discardedProductsIdsFromIntent)) {
            productsIdsFromIntentList =  i.getStringExtra(discardedProductsIdsFromIntent);

            getItems();
        }else
            getItems();
    }

    @Override
    protected void checkItemsFromIntent() {
        if(productsIdsFromIntentList != null && productsIdsFromIntentList.length()>1){
            model.setFilter(getNewFilter());
            model.getFilter().setIds( getList(productsIdsFromIntentList) );
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
                new AlertDialog.Builder(ProductsMainActivity.this)
                        .setTitle(event).setMessage(newMsg+"\n\n"+"show discarded products separately ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                productsIdsFromIntentList =  message;
                                getItems();
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Long> ids = getList( message );
                        for(Product product:items){
                            for(Long discardedProductId : ids){
                                if(product.getId() == discardedProductId)
                                {
                                    ids.remove(discardedProductId);
                                    product.setDiscarded(true);
                                    break;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
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
                Intent toEmployees=new Intent(ProductsMainActivity.this, EmployeesMainActivity.class);
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


package com.example.inventoryui.Activities.Test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Employees.EmployeesMainActivity;
import com.example.inventoryui.Activities.Products.ProductAddActivity;
import com.example.inventoryui.Activities.Products.ProductsAdapter;
import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.DataAccess.TestData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test2Activity extends BaseMainActivity<Product,IndexVM,FilterVM, TestData> {

    String discardedProductsIdsFromIntent = "discardedProductsIds";
    String productsIdsFromIntentList;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsAdapter(this, super.items, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item) {
                Intent i = new Intent(Test2Activity.this, ProductAddActivity.class);
                i.putExtra("productForUpdate", item);
                startActivity(i);
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item) {

            }
        });
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    protected IndexVM getNewIndexVM() {
        return new IndexVM();
    }

    @Override
    protected void checkItemsFromIntent() {
        if(productsIdsFromIntentList != null && productsIdsFromIntentList.length()>1){
           model.setFilter(getNewFilter());
           /***********/
            model.getFilter().setIds( getList(productsIdsFromIntentList) );
            }
    }

   /* @Override
    protected Class getFilterClass() {
        return FilterVM.class;
    }*/

    @Override
    protected TestData getItemData() {
        return  new ViewModelProvider(this).get(TestData.class);
    }

    @Override
    protected FilterVM getNewFilter() {
        return new FilterVM();
    }

    private void addFabOnClick() {
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Test2Activity.this, ProductAddActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.products_menu,menu);
        if(loggedUser.getRole().equals(Role.ROLE_Employee)){
            // menu.findItem(R.id.employees).setVisible(false);
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                ( (AuthenticationManager)this.getApplication()).logout();
                /*Intent i=new Intent(ProductsMainActivity.this, MainActivity.class);
                startActivity(i);*/
                return true;
            case R.id.employees:
                //to employees
                Intent toEmployees=new Intent(Test2Activity.this, EmployeesMainActivity.class);
                startActivity(toEmployees);
                return true;
           /* case R.id.all:
               itemData.getProductsForUser();
                return true;
            case R.id.dma:
               itemData.getProductByType(ProductType.DMA);
                return true;
            case R.id.ma:
               itemData.getProductByType(ProductType.MA);
                return true;
            case R.id.discarded:
                itemData.getDiscardedProducts(true);
                return true;
            case R.id.notDiscarded:
               itemData.getDiscardedProducts(false);
                return true;
            case R.id.available:
                itemData.getAvailableProductsForUser(true);
                return true;
            case R.id.missing:
                productsData.getAvailableProductsForUser(false);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

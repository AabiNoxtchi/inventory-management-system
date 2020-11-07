package com.example.inventoryui.Activities.Products;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Employees.EmployeesMainActivity;
import com.example.inventoryui.Activities.MainActivity;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product;
import com.example.inventoryui.Models.ProductType;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductsMainActivity extends AppCompatActivity {

    final String TAG="MyActivity_ProductsMain";
    FloatingActionButton addFab;
    RecyclerView productsRecyclerView ;
    ProductsAdapter productsAdapter;
    ProductsData productsData;
    ArrayList<Product> products;
    String discardedProductsIdsFromIntentName = "discardedProductsIds";
    String productsIdsFromIntentList;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);

        loggedUser=((AuthenticationManager)this.getApplication()).getLoggedUser();
        if(loggedUser.getRole().equals(Role.ROLE_Mol))
        {
            addFab=findViewById(R.id.addFab);
            addFab.show();
            addFabOnClick();
        }

        productsData= new ViewModelProvider(this).get(ProductsData.class);

        productsData.getResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
               //----------testing-----------//
            }
        });

        productsRecyclerView=findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        products=new ArrayList<>();
        productsAdapter=new ProductsAdapter(ProductsMainActivity.this, products, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item) {
                Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                i.putExtra("productForUpdate", item);
                startActivity(i);
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item) {

            }
        });
        productsRecyclerView.setAdapter(productsAdapter);
        //get info from intent
        Intent i=getIntent();
        if(i.hasExtra(discardedProductsIdsFromIntentName)) {
            productsIdsFromIntentList =  i.getStringExtra(discardedProductsIdsFromIntentName);
            getProducts(null);
        }else if(loggedUser.getRole().equals(Role.ROLE_Employee))
            getProducts(loggedUser.getId());
        else
            getProducts(null);
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

    private void getProducts(@Nullable Long employeeId) {
        productsData.getAllProductsForUser(null,null, null,employeeId, productsIdsFromIntentList)
                .observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> newProducts) {
               products.clear();
               products.addAll(newProducts);
               productsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateUifromThread(final String event, final String newMsg, final String message){
       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                         new AlertDialog.Builder(ProductsMainActivity.this)
                        .setTitle(event).setMessage(newMsg+"\n\n"+"show discarded products ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                productsIdsFromIntentList =  message;
                                getProducts(null);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message2=message.substring(1, message.length() - 1);
                                List<String> ids = new ArrayList<>(Arrays.asList(message2.split(",")));

                                for(Product product:products){
                                    for(String discardedProductId : ids){
                                        if(product.equalsByStringId(discardedProductId))
                                        {
                                            ids.remove(discardedProductId);
                                            product.setDiscarded(true);
                                            break;
                                        }
                                    }
                                }
                                productsAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.products_menu,menu);
        if(loggedUser.getRole().equals(Role.ROLE_Employee)){
            menu.findItem(R.id.employees).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                ( (AuthenticationManager)this.getApplication()).logout();
                Intent i=new Intent(ProductsMainActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.employees:
                //to employees
                Intent toEmployees=new Intent(ProductsMainActivity.this, EmployeesMainActivity.class);
                startActivity(toEmployees);
                return true;
            case R.id.all:
                productsData.getProductsForUser();
                return true;
            case R.id.dma:
                productsData.getProductByType(ProductType.DMA);
                return true;
            case R.id.ma:
                productsData.getProductByType(ProductType.MA);
                return true;
            case R.id.discarded:
                productsData.getDiscardedProducts(true);
                return true;
            case R.id.notDiscarded:
                productsData.getDiscardedProducts(false);
                return true;
            case R.id.available:
                productsData.getAvailableProductsForUser(true);
                return true;
            case R.id.missing:
                productsData.getAvailableProductsForUser(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthenticationManager.activityResumed();
        AuthenticationManager.setActiveActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AuthenticationManager.activityPaused();
        AuthenticationManager.setActiveActivity(null);
    }
}


package com.example.inventoryui.Activities.Products;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Product.ProductType;
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
    String discardedProductsIdsFromIntent = "discardedProductsIds";
    String productsIdsFromIntentList;
    User loggedUser;
    IndexVM model;

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
        Intent i=getIntent();
        if(i.hasExtra(discardedProductsIdsFromIntent)) {
            productsIdsFromIntentList =  i.getStringExtra(discardedProductsIdsFromIntent);
            getProducts();
        }else
            getProducts();
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

    private void getProducts() {

        model = new IndexVM();
        model.setFilter(new FilterVM());
        if(productsIdsFromIntentList != null && productsIdsFromIntentList.length()>1)
        {
            model.getFilter().setIds( getList(productsIdsFromIntentList) );
        }

        productsData.getAll( model)
                .observe(this, new Observer<IndexVM>() {
                    @Override
                    public void onChanged(IndexVM indexVM) {
                        model = indexVM;
                        products.clear();
                        products.addAll(indexVM.getItems());
                        productsAdapter.notifyDataSetChanged();
                    }
                } );
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
                                getProducts();
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Long> ids = getList( message );
                                for(Product product:products){
                                    for(Long discardedProductId : ids){
                                        if(product.getId() == discardedProductId)
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


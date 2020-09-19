package com.example.inventoryui.Controllers.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Controllers.Employees.EmployeesMainActivity;
import com.example.inventoryui.Controllers.MainActivity;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product;
import com.example.inventoryui.Models.ProductType;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductsMainActivity extends AppCompatActivity {

    FloatingActionButton addFab;
    RecyclerView productsRecyclerView ;
    ProductsAdapter productsAdapter;
    private ProductsData productsData;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);

        addFab=findViewById(R.id.addFab);
        addFab.show();
        addFabOnClick();

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
                //Toast.makeText(ProductsMainActivity.this,"clicked on final listner",Toast.LENGTH_LONG).show();
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
        productsData.getAllProductsForUser(null,null, null,null).observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> newProducts) {
               products.clear();
               products.addAll(newProducts);
               productsAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.products_menu,menu);
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
}


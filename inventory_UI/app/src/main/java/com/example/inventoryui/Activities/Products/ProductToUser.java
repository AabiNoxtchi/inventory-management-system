package com.example.inventoryui.Activities.Products;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.User.UserAddActivity;
import com.example.inventoryui.Activities.User.UsersMainActivity;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Shared.PagerVM;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductToUser extends AppCompatActivity {

    private static final String TAG = "MyActivity_EmployeeAdd";
    TextView employeeNameAdd;
    TextView employeeUserNameAdd;
    TextView employeeProductsLabel;
    FloatingActionButton addProductForEmployeeFab;

    UsersData usersData;
    User employeeFromIntent;

    RecyclerView productsListView;
    ProductsAdapter productsAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<Product> products;
    ProductsData productsData;

    ArrayAdapter<Product> spinnerAdapter;
    private Spinner spinnerProducts;
    List<Product> spinnerProductsList;


    Product selectedProductFromSpinner;
    Product selectedProductFromListView;

    ArrayList<Long> idsToDelete;
    int idsToDeleteCount = 0;
    ActionMode actionMode;
    String actionModeTitle = "items selected ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_to_user);

        Toolbar toolbar = findViewById(R.id.toolbar_addEmployee);
        setSupportActionBar(toolbar);

        employeeNameAdd=findViewById(R.id.employeeNameAdd);
        employeeUserNameAdd=findViewById(R.id.employeeUserNameAdd);
        employeeProductsLabel=findViewById(R.id.employeeProductsLabel);
        addProductForEmployeeFab=findViewById(R.id.addFabProductForEmployee);

        productsData= new ViewModelProvider(this).get(ProductsData.class);
        productsListView=findViewById(R.id.productsList);
        layoutManager = new LinearLayoutManager(this);
        productsListView.setLayoutManager(layoutManager);
        products=new ArrayList<>();
        productsAdapter = new ProductsAdapter(this, products, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item, int position) {

                if ( productsAdapter.multiSelect) {

                    ifAdapterMultiSelect(item, position );

                }
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item, int position) {

                onLongClick(item,position);

            }
        }, getActionMode());

        productsListView.setAdapter(productsAdapter);

        usersData = new ViewModelProvider(this).get(UsersData.class);

        spinnerProducts=findViewById(R.id.spinnerProducts);
        spinnerProductsList=new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerProductsList);

        Intent i=getIntent();
        if(i.hasExtra("userForUpdate")) {
            employeeFromIntent = (User) i.getSerializableExtra("userForUpdate");
            addProductForEmployeeFab.setEnabled(true);
            initializeFields();
        }else if(i.hasExtra("employeeIdForUpdate")) {//employeeIdForUpdate
            long id = (long) i.getLongExtra("employeeIdForUpdate", 0);
            usersData.getById(id);
            usersData.getItem().observe(ProductToUser.this, new Observer<User>() {
                @Override
                public void onChanged(User employee) {
                    employeeFromIntent = employee;
                    initializeFields();
                }
            });
        }

        productsData.getNullifiedIds().observe(ProductToUser.this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                actionMode.finish();
                for(Long id : longs){
                    Product toSwitch = products.stream().filter(p -> p.getId().equals(id)).findAny().orElse(null);
                    products.remove(toSwitch);
                    spinnerProductsList.add(toSwitch);
                }
                productsAdapter.notifyDataSetChanged();
                spinnerAdapter.notifyDataSetChanged();

            }
        });

        productsData.getSavedId().observe(ProductToUser.this, new Observer<Product>() {
            @Override
            public void onChanged(Product response) {
                if (response != null) {
                    // if product.employeeId == employeeFromIntentId
                    // add to employee list view and remove from spinner
                    if( Objects.equals(employeeFromIntent.getId(), response.getEmployee_id())){
                        Product productToEmployee=selectedProductFromSpinner;
                        products.add(productToEmployee);
                        productsAdapter.notifyDataSetChanged();
                        spinnerProductsList.remove(productToEmployee);
                        spinnerAdapter.notifyDataSetChanged();
                        selectedProductFromSpinner = null;
                        employeeProductsLabel.setVisibility(View.VISIBLE);
                        spinnerProducts.setVisibility(View.GONE);
                        addProductForEmployeeFab.setImageResource(R.drawable.ic_add_black_24dp);
                    }else {  //// add to spinner and remove from  employee list view
                        Product productToSpinner=new Product();
                        productToSpinner=selectedProductFromListView;
                        products.remove(productToSpinner);
                        productsAdapter.notifyDataSetChanged();
                        spinnerProductsList.add(productToSpinner);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getApplication(), "error couldn't add product !!! ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ifAdapterMultiSelect(Product item, int position){
        item.toggleSelected();
        if (item.isSelected()) {
            checkItem(item, position);
        } else {
            idsToDelete.remove(item.getId());
            idsToDeleteCount--;
            productsAdapter.notifyItemChanged(position);
            actionMode.setTitle(actionModeTitle + idsToDeleteCount);
        }
    }

    protected void checkItem(Product item, int position){
        idsToDelete.add(item.getId());
        idsToDeleteCount++;
        productsAdapter.notifyItemChanged(position);
        actionMode.setTitle(actionModeTitle + idsToDeleteCount);
    }

    protected void onLongClick(Product item, int position){
        idsToDelete = new ArrayList<>();
        item.setSelected(true);
        checkItem(item, position);
    }

    protected ActionMode.Callback getActionMode(){
        return
                new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                        MenuInflater inflater=mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_option_delete,menu);
                        actionMode = mode;
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.DeleteAllBtn :

                                productsData.nullifyIds(idsToDelete);
                               // onDestroyActionMode(mode);
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                        productsAdapter.setMultiSelect(false);

                        if(idsToDeleteCount > 0) {
                            products.stream().forEach(i -> i.setSelected(false));
                            productsAdapter.notifyDataSetChanged();
                            idsToDeleteCount = 0;
                            idsToDelete = null;
                        }
                    }
                };
    }

    private void addProductForEmployeeFabOnClick() {
        addProductForEmployeeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinnerProducts.getVisibility()==View.GONE)
                {
                    employeeProductsLabel.setVisibility(View.GONE);
                    spinnerProducts.setVisibility(View.VISIBLE);
                    spinnerProducts.setSelection(0);
                    addProductForEmployeeFab.setImageResource(R.drawable.save);
                }
                else if(selectedProductFromSpinner != null) {
                    if(products.indexOf(spinnerProducts.getSelectedItem())!=-1){
                        new AlertDialog.Builder(ProductToUser.this)
                                .setTitle("").setMessage(employeeFromIntent.getFirstName()+" already has this product !!!")
                                .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        return;
                                    }
                                }).show();
                    }else {
                        selectedProductFromSpinner.setEmployee_id(employeeFromIntent.getId());
                         productsData.save(selectedProductFromSpinner);
                    }
                }

            }
        });
    }

    private void initializeFields() {
        Log.i(TAG,"initializing fields");
        employeeNameAdd.setText("Name : "+employeeFromIntent.getFirstName()+" "+employeeFromIntent.getLastName());
        employeeNameAdd.setFocusable(false);
        employeeNameAdd.setEnabled(true);
        employeeUserNameAdd.setText("User Name : "+employeeFromIntent.getUserName());
        employeeUserNameAdd.setFocusable(false);
        employeeUserNameAdd.setEnabled(true);
        observeProductsData();  ///////// getProducts + fill spinner from here  //////
        spinnerOnClick();
        addProductForEmployeeFabOnClick();

    }

    private void spinnerOnClick() {
        spinnerProducts.setAdapter(spinnerAdapter);
        spinnerProducts.setSelection(0);
        spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    selectedProductFromSpinner = (Product) parent.getSelectedItem();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void observeProductsData() {

        IndexVM productsIndex = new IndexVM();
        productsIndex.setPager(new PagerVM());
        productsIndex.getPager().setItemsPerPage(Integer.MAX_VALUE);
        productsIndex.setFilter(new FilterVM());
        if (employeeFromIntent != null) {
            productsIndex.getFilter().getUrlParameters().put("employeeIdOrFree",true);//.setEmployeeId(employeeFromIntent.getId());
            productsIndex.getFilter().getUrlParameters().put("employeeId",employeeFromIntent.getId());

        }
        productsData.getAll(productsIndex);
        productsData.getIndexVM().observe(this, new Observer<IndexVM>() {
            @Override
            public void onChanged(IndexVM indexVM) {
                Log.i(TAG, "***************product observer changed");
                for (Product product : indexVM.getItems()) {
                    Log.i(TAG, "product.getEmployee_id()==null = "+(product.getEmployee_id()==null));
                    Log.i(TAG, "(employeeFromIntent != null) = "+(employeeFromIntent != null));
                    if(product.getId()!=null) {
                        Log.i(TAG, "product.getEmployee_id() = " + (product.getEmployee_id()));
                        Log.i(TAG, "(Objects.equals(product.getEmployee_id(), employeeFromIntent.getId())) = "
                                + (Objects.equals(product.getEmployee_id(), employeeFromIntent.getId())));
                    }
                    if (product.getEmployee_id() == null)
                        spinnerProductsList.add(product);
                    else if (employeeFromIntent != null) {
                        if (Objects.equals(product.getEmployee_id(), employeeFromIntent.getId()))
                            products.add(product);
                        Log.i(TAG,"products.size = "+products.size());
                        productsAdapter.notifyItemInserted(products.indexOf(product));
                    }
                }
                productsAdapter.notifyDataSetChanged();
                spinnerProductsList.add(0, new Product("---select product---"));
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.user_add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_delete_user:
                new AlertDialog.Builder(ProductToUser.this)
                        .setTitle("Delete User").setMessage(" sure you want to delete "
                        + employeeFromIntent.getUserName()+" ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //// delete employee
                                usersData.deleteId(employeeFromIntent.getId());
                                usersData.getDeletedId().observe(ProductToUser.this, new Observer<Long>() {
                                    @Override
                                    public void onChanged(Long aLong) {
                                        if(Objects.equals(aLong,employeeFromIntent.getId())) {
                                            Toast.makeText(ProductToUser.this, "User has been deleted !!!", Toast.LENGTH_LONG);
                                            Intent i = new Intent(ProductToUser.this, UsersMainActivity.class);
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(ProductToUser.this, "User could't be deleted !!!", Toast.LENGTH_LONG);
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
                return true;
            case R.id.icon_edit_user:
                Intent i = new Intent(ProductToUser.this, UserAddActivity.class);
                i.putExtra("userForUpdate", employeeFromIntent);
                startActivity(i);
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

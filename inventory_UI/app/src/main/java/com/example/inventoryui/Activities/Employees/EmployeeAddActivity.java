package com.example.inventoryui.Activities.Employees;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Activities.Admin.AdminAddMolActivity;
import com.example.inventoryui.DataAccess.EmployeesData;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Employee;
import com.example.inventoryui.Models.Product;
import com.example.inventoryui.Models.UpdatedProductResponse;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeAddActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity_EmployeeAdd";
    TextView employeeNameAdd;
    TextView employeeUserNameAdd;
    TextView employeeProductsLabel;
    FloatingActionButton addProductForEmployeeFab;

    EmployeesData employeesData;
    Employee employeeFromIntent;
    ListView productsListView;
    ArrayAdapter productsAdapter;
    ArrayList<Product> products;
    ProductsData productsData;

    ArrayAdapter<Product> spinnerAdapter;
    private Spinner spinnerProducts;
    List<Product> spinnerProductsList;
    Product selectedProductFromSpinner;
    Product selectedProductFromListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);

        employeeNameAdd=findViewById(R.id.employeeNameAdd);
        employeeUserNameAdd=findViewById(R.id.employeeUserNameAdd);
        employeeProductsLabel=findViewById(R.id.employeeProductsLabel);
        addProductForEmployeeFab=findViewById(R.id.addFabProductForEmployee);

        productsListView=findViewById(R.id.productsList);
        productsData= new ViewModelProvider(this).get(ProductsData.class);
        products=new ArrayList<>();
        productsAdapter = new ArrayAdapter<>(this,R.layout.listview_itemtostring_card, products);
        productsListView.setAdapter(productsAdapter);

        employeesData=new ViewModelProvider(this).get(EmployeesData.class);

        spinnerProducts=findViewById(R.id.spinnerProducts);
        spinnerProductsList=new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerProductsList);

        Intent i=getIntent();
        if(i.hasExtra("employeeForUpdate")) {
            employeeFromIntent = (Employee) i.getSerializableExtra("employeeForUpdate");
            addProductForEmployeeFab.setEnabled(true);
            initializeFields();
        }else if(i.hasExtra("employeeIdForUpdate")) {
            long id = (long) i.getLongExtra("employeeIdForUpdate", 0);
            employeesData.getEmployeeById(id);
            employeesData.getEmployeeById().observe(EmployeeAddActivity.this, new Observer<Employee>() {
                @Override
                public void onChanged(Employee employee) {
                    employeeFromIntent = employee;
                    initializeFields();
                }
            });
        }

        productsData.getUpdatedProduct().observe(EmployeeAddActivity.this, new Observer<UpdatedProductResponse>() {
            @Override
            public void onChanged(UpdatedProductResponse response) {
                if (response != null) {
                    // if product.employeeId == employeeFromIntentId
                    // add to employee list view and remove from spinner
                    if( Objects.equals(employeeFromIntent.getId(), response.getEmployeeId())){
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

    private void productsListViewOnLongClick() {
        productsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Product product = products.get(position);
                new AlertDialog.Builder(EmployeeAddActivity.this)
                        .setTitle("Delete Product").setMessage(" sure you want to delete "
                        + product.getName()+" from "+employeeFromIntent.getFirstName()+"'s products !!!")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //// delete employee from product
                                productsData.updateProduct(product,(long)0);
                                selectedProductFromListView=product;
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();

                return true;
            }
        });
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
                        new AlertDialog.Builder(EmployeeAddActivity.this)
                                .setTitle("").setMessage(employeeFromIntent.getFirstName()+" already has this product !!!")
                                .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        return;
                                    }
                                }).show();
                    }else {
                        productsData.updateProduct(selectedProductFromSpinner, employeeFromIntent.getId());
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
        insertProductObserver();
        spinnerOnClick();
        addProductForEmployeeFabOnClick();
        updateEmployeeObserver();
        productsListViewOnLongClick();
        observeProductsData();  ///////// getProducts + fill spinner from here  //////
    }

    private void insertProductObserver() {
        employeesData.getInsertedEmployee().observe(this, new Observer<Boolean>(){
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(getApplication()," 1 employee have been saved successfully ", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EmployeeAddActivity.this, EmployeesMainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplication(),"error couldn't save employee !!! ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateEmployeeObserver() {
        employeesData.getUpdatedEmployee().observe(this, new Observer<Boolean>(){
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(getApplication()," 1 employee have been updated successfully ", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(EmployeeAddActivity.this, EmployeesMainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplication(),"error couldn't update employee !!! ",Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private void observeProductsData(){
        productsData.getAllProductsForUser(null, null, null,
                null, null).observe(this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> newProducts) {
                Log.i(TAG,"product observer changed");
                for (Product product : newProducts) {
                    if(product.getEmployee_id()==null)
                        spinnerProductsList.add(product);
                    else
                    if(employeeFromIntent!=null){
                        if(Objects.equals(product.getEmployee_id(),employeeFromIntent.getId()))
                            products.add(product);
                    } productsAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.employee_add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_delete_employee:
                new AlertDialog.Builder(EmployeeAddActivity.this)
                        .setTitle("Delete User").setMessage(" sure you want to delete "
                        + employeeFromIntent.getUserName()+" ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //// delete employee
                                employeesData.getDeleted(employeeFromIntent.getId());
                                employeesData.getDeleted().observe(EmployeeAddActivity.this, new Observer<Long>() {
                                    @Override
                                    public void onChanged(Long aLong) {
                                        if(Objects.equals(aLong,employeeFromIntent.getId())) {
                                            Toast.makeText(EmployeeAddActivity.this, "User has been deleted !!!", Toast.LENGTH_LONG);
                                            Intent i = new Intent(EmployeeAddActivity.this, EmployeesMainActivity.class);
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(EmployeeAddActivity.this, "User could't be deleted !!!", Toast.LENGTH_LONG);
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
            case R.id.icon_edit_employee:
                Intent i = new Intent(EmployeeAddActivity.this, AdminAddMolActivity.class);
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


package com.example.inventoryui.Activities.Employees;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.DataAccess.EmployeesData;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Employee;
import com.example.inventoryui.Models.Product;
import com.example.inventoryui.Models.UpdatedProductResponse;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeAddActivity extends AppCompatActivity {

    TextView employeeFirstNameAdd;
    TextView employeeLastNameAdd;
    Button btn_save_employee;
    Button btn_cancel_employee;
    TextView employeeProductsLabel;
    TextInputLayout employeeFirstNameAddTextLayout;
    TextInputLayout employeeLastNameAddTextLayout;

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

    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);

        employeeFirstNameAdd=findViewById(R.id.employeeFirstNameAdd);
        employeeLastNameAdd=findViewById(R.id.employeeLastNameAdd);
        employeeProductsLabel=findViewById(R.id.employeeProductsLabel);
        employeeFirstNameAddTextLayout=findViewById(R.id.employeeFirstNameAddTextLayout);
        employeeLastNameAddTextLayout=findViewById(R.id.employeeLastNameAddTextLayout);
        btn_save_employee=findViewById(R.id.btn_save_employee);
        btn_cancel_employee=findViewById(R.id.btn_cancel_employee);

        addProductForEmployeeFab=findViewById(R.id.addFabProductForEmployee);

        productsListView=findViewById(R.id.productsList);
        productsData= new ViewModelProvider(this).get(ProductsData.class);
        products=new ArrayList<>();
        productsAdapter = new ArrayAdapter<Product>(this,R.layout.listview_itemtostring_card, products);
        productsListView.setAdapter(productsAdapter);

        employeesData=new ViewModelProvider(this).get(EmployeesData.class);

        Intent i=getIntent();
        if(i.hasExtra("employeeForUpdate")) {
            employeeFromIntent = (Employee) i.getSerializableExtra("employeeForUpdate");
           // if(employeeFromIntent!=null)getProducts();
            addProductForEmployeeFab.setEnabled(true);
            initializeFields();
        }else{
            addProductForEmployeeFab.setEnabled(false);
            employeeProductsLabel.setText("employee must be saved first");
        }

        spinnerProducts=findViewById(R.id.spinnerProducts);
        spinnerProductsList=new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerProductsList);

        btnSaveOnClick();
        btnCancelOnClick();
        insertProductObserver();
        spinnerOnClick();
        addProductForEmployeeFabOnClick();
        updateEmployeeObserver();
        productsListViewOnLongClick();
        observeProductsData();//getProducts+fill spinner from here


        productsData.getUpdatedProduct().observe(EmployeeAddActivity.this, new Observer<UpdatedProductResponse>() {
            @Override
            public void onChanged(UpdatedProductResponse response) {
                if (response != null) {
                   // Product productToEmployee=new Product(response.getId(),response.getProductName());
                   // Log.i(TAG,"response.employee id = "+response.getEmployeeId());
                  //  Log.i(TAG,"employee from intent id = "+employeeFromIntent.getId());

                    if(response.getEmployeeId()!=null) {
                        Log.i(TAG, "response.getEmployeeId()!=null : " + (response.getEmployeeId() != null));
                        Log.i(TAG, "employee id from response : " + response.getEmployeeId());
                        Log.i(TAG, "employee id from intent : " + employeeFromIntent.getId());
                        Log.i(TAG, "response id type : " + response.getEmployeeId().getClass().toString());
                        Log.i(TAG, "intent employee id type : " + employeeFromIntent.getId().getClass().toString());
                        Log.i(TAG, "response.getEmployeeId()==employeeFromIntent.getId() : " + (response.getEmployeeId() == employeeFromIntent.getId()));
                        Log.i(TAG,"object equals : "+ Objects.equals(employeeFromIntent.getId(), response.getEmployeeId()));
                        boolean validId = (response.getEmployeeId() != null && response.getEmployeeId() == employeeFromIntent.getId());
                        Toast.makeText(getApplication(), "valid id = " + validId, Toast.LENGTH_LONG).show();
                    }

                    // add to employee list view
                   // if(response.getEmployeeId()!=null&&response.getEmployeeId()==employeeFromIntent.getId()) {
                    if( Objects.equals(employeeFromIntent.getId(), response.getEmployeeId())){
                        Log.i(TAG,"got through validation ");

                       // Toast.makeText(getApplication(),""+response.getEmployeeId()+" "+employeeFromIntent.getId(),Toast.LENGTH_LONG).show();
                        Product productToEmployee=new Product();
                        productToEmployee=selectedProductFromSpinner;
                        products.add(productToEmployee);
                        productsAdapter.notifyDataSetChanged();

                        spinnerProductsList.remove(productToEmployee);
                        spinnerAdapter.notifyDataSetChanged();
                        selectedProductFromSpinner = null;
                        employeeProductsLabel.setVisibility(View.VISIBLE);
                        spinnerProducts.setVisibility(View.GONE);
                        addProductForEmployeeFab.setImageResource(R.drawable.ic_add_black_24dp);
                    }else {// add to spinner
                        Log.i(TAG,"in else !");

                        Product productToSpinner=new Product();
                        productToSpinner=selectedProductFromListView;
                    // final Product product=products.get(position);
                    products.remove(productToSpinner);
                    productsAdapter.notifyDataSetChanged();
                   // Product newProduct = new Product(response.getId(),response.getProductName());
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
                //Toast.makeText(EmployeeAddActivity.this,"position "+position+" id"+id,Toast.LENGTH_LONG).show();
                AlertDialog alert = new AlertDialog.Builder(EmployeeAddActivity.this)
                        .setTitle("Delete Product").setMessage(" sure you want to delete "
                                +product.getName()+" from "+employeeFromIntent.getFirstName()+"'s products !!!")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //delete

                                try {
                                    productsData.updateProduct(product,null);
                                    selectedProductFromListView=product;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                        AlertDialog alert = new AlertDialog.Builder(EmployeeAddActivity.this)
                                .setTitle("").setMessage(employeeFromIntent.getFirstName()+" already has this product !!!")
                                .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        return;
                                    }
                                }).show();
                    }else {


                        try {
                            productsData.updateProduct(selectedProductFromSpinner, employeeFromIntent.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }
        });
    }



    private void initializeFields() {
        btn_cancel_employee.setVisibility(View.GONE);
        btn_save_employee.setVisibility(View.GONE);

       // employeeFirstNameAdd.setText("");
        employeeFirstNameAddTextLayout.setHint("");
        employeeFirstNameAdd.setText(employeeFromIntent.getFirstName()+" "+employeeFromIntent.getLastName());

        employeeFirstNameAdd.setFocusable(false);
        employeeFirstNameAdd.setEnabled(true);
        employeeLastNameAdd.setVisibility(View.GONE);
        employeeLastNameAddTextLayout.setVisibility(View.GONE);
        //employeeLastNameAdd.setFocusable(false);
        employeeLastNameAdd.setEnabled(false);

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

    private void btnSaveOnClick() {
        btn_save_employee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean valid=true;

                   if(employeeFirstNameAdd.getText().toString().length()<3) {
                       employeeFirstNameAdd.setError("too short");
                       valid=false;
                   }
                   if(employeeLastNameAdd.getText().toString().length()<3) {
                       employeeLastNameAdd.setError("too short");
                       valid=false;
                   }
                   if(valid) {
                       Employee employee=new Employee();
                       employee.setFirstName(employeeFirstNameAdd.getText().toString());
                       employee.setLastName(employeeLastNameAdd.getText().toString());
                       try {
                           if (employeeFromIntent != null) {
                               employee.setId(employeeFromIntent.getId());
                               employeesData.updateEmployee(employee);
                           } else
                               employeesData.insertEmployee(employee);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
            }
        });
    }

    private void btnCancelOnClick() {
        btn_cancel_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeAddActivity.this, EmployeesMainActivity.class);
                startActivity(i);
            }
        });
    }

    private void spinnerOnClick() {
        spinnerProducts.setAdapter(spinnerAdapter);
        spinnerProducts.setSelection(0);

        //
       /* productsData.getAllProductsForUser(null, null, null,
                null);*/

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
    private void getProducts() {
        // productsData.getProductsForEmployee(employeeFromIntent.getId());
        productsData.getProductsForEmployee(employeeFromIntent.getId());

    }

      private void observeProductsData(){

                  productsData.getAllProductsForUser(null, null, null,
                          null, null).observe(this, new Observer<ArrayList<Product>>() {
                      @Override
                      public void onChanged(ArrayList<Product> newProducts) {


                              for (Product product : newProducts) {
                                  if(product.getEmployee()==null)
                                      spinnerProductsList.add(product);
                                  else
                                  if(employeeFromIntent!=null){
                                      if(Objects.equals(product.getEmployee().getId(),employeeFromIntent.getId()))
                                          products.add(product);
                              } productsAdapter.notifyDataSetChanged();
                              }
                          productsAdapter.notifyDataSetChanged();
                          spinnerProductsList.add(0, new Product("---select product---"));
                          spinnerAdapter.notifyDataSetChanged();

                         /* products.clear();
                          products.addAll(newProducts);
                          productsAdapter.notifyDataSetChanged();
                         // Log.i(TAG, "triggered getProducts() ");

                          spinnerProductsList.clear();
                          try {
                              newProducts.removeAll(products);
                          } catch (NullPointerException e) {
                              System.out.println("Exception thrown : " + e);
                          }
                          spinnerProductsList.addAll(newProducts);
                          spinnerProductsList.add(0, new Product("---select product---"));
                          spinnerAdapter.notifyDataSetChanged();*/
                         // Log.i(TAG, "triggered fill spinner ");
                      }
                  });


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


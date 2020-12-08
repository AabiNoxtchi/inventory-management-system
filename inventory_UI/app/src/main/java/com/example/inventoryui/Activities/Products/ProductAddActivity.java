package com.example.inventoryui.Activities.Products;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.HelperFilters.MinMaxValueFilter;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Product.ProductType;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProductAddActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity_ProductAdd";

     RadioGroup productTypeRadioGroup;
     RadioButton DMARadioButton;
     RadioButton MARadioButton;
     TextView productNameTextView;
     TextView inventoryNumberTextView;
     TextView dateCreatedTextView;
     TextView descriptionTextView;
     CheckBox isAvailableCheckBox;
     CheckBox isDiscardedCheckBox;
     TextView yearsToDiscardTextView;
     TextView amortizationPercentTextView;
     TextView yearsToMAConvertionTextView;
     TextInputLayout yearsToMAConvertion_TextInputLayout;
     TextInputLayout amortizationPercent_TextInputLayout;
     TextInputLayout yearsToDiscard_TextInputLayout;
     Spinner employeeSpinner;
     Button btnSave;
     Button btnCancel;
     TextView titleTextView;

     ProductsData productsData;
     Product productFromIntent;
     User loggedUser;

    final SimpleDateFormat ft= new SimpleDateFormat("E yyyy.MM.dd ", Locale.ENGLISH);
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        Toolbar toolbar = findViewById(R.id.toolbar_addProduct);
        setSupportActionBar(toolbar);

        loggedUser=((AuthenticationManager)this.getApplication()).getLoggedUser();
        productTypeRadioGroup=findViewById(R.id.productTypeRadioGroup);
        DMARadioButton=findViewById(R.id.DMARadioButton);
        MARadioButton=findViewById(R.id.MARadioButton);
        productNameTextView=findViewById(R.id.productNameEditText);
        inventoryNumberTextView=findViewById(R.id.inventoryNumberEditText);
        dateCreatedTextView=findViewById(R.id.dateCreatedEditText);
        descriptionTextView=findViewById(R.id.descriptionEditText);
        isAvailableCheckBox=findViewById(R.id.isAvailableCheckBox);
        isDiscardedCheckBox=findViewById(R.id.isDiscardedCheckBox);
        yearsToDiscardTextView=findViewById(R.id.yearsToDiscard_EditText);
        yearsToMAConvertionTextView=findViewById(R.id.yearsToMAConvertion_EditText);
        amortizationPercentTextView=findViewById(R.id.amortizationPercent_EditText);

        yearsToMAConvertion_TextInputLayout=findViewById(R.id.yearsToMAConvertion_TextInputLayout);
        amortizationPercent_TextInputLayout=findViewById(R.id.amortizationPercent_TextInputLayout);
        yearsToDiscard_TextInputLayout=findViewById(R.id.yearsToDiscard_TextInputLayout);

        employeeSpinner=findViewById(R.id.employeeSpinner);
        btnSave=findViewById(R.id.btn_save);
        btnCancel=findViewById(R.id.btn_cancel);
        titleTextView = findViewById(R.id.titleTextView);


        amortizationPercentTextView.setFilters( new InputFilter[]{ new MinMaxValueFilter( "1" , "100" )});
        productsData= new ViewModelProvider(this).get(ProductsData.class);

        Intent i=getIntent();
        if(i.hasExtra("productForUpdate")) {
            productFromIntent = (Product) i.getSerializableExtra("productForUpdate");
            initializeFields();
        }

       if(productFromIntent == null) dateCreatedTextView.setText(ft.format(new Date()));

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateCreatedTextView.setText(ft.format(myCalendar.getTime()));
            }
        };

        productTypeRadioGroupOnClick();
        isDiscardedCheckBoxOnClick();
        dateCreatedTextViewOnClick();

        btnSaveOnClick();
        btnCancelOnClick();
        deleteObserver();
        savedProductObserver();
    }

    private void initializeFields() {

        titleTextView.setText("Edit Product");
        productNameTextView.setText(productFromIntent.getName());
        inventoryNumberTextView.setText(productFromIntent.getInventoryNumber());
        descriptionTextView.setText(productFromIntent.getDescription());
        dateCreatedTextView.setText(ft.format(productFromIntent.getDateCreated()));
        isAvailableCheckBox.setChecked(productFromIntent.isAvailable());
        isDiscardedCheckBox.setChecked(productFromIntent.isDiscarded());
        if(!productFromIntent.isDiscarded()){
            yearsToDiscardTextView.setText(String.valueOf(productFromIntent.getYearsToDiscard()));
        }else{
            yearsToDiscard_TextInputLayout.setVisibility(View.GONE);
        }
        ProductType productType=productFromIntent.getProductType();
        if(productType==ProductType.DMA) {
            DMARadioButton.setChecked(true);
            yearsToMAConvertionTextView.setText(productFromIntent.getYearsToMAConvertion().toString());
            amortizationPercentTextView.setText(productFromIntent.getAmortizationPercent().toString());
        }else {
            MARadioButton.setChecked(true);
            amortizationPercent_TextInputLayout.setVisibility(View.GONE);
            yearsToMAConvertion_TextInputLayout.setVisibility(View.GONE);
        }
        btnSave.setText("Update");

        if(loggedUser.getRole().equals(Role.ROLE_Employee))
        {
            productNameTextView.setFocusable(false);
            inventoryNumberTextView.setFocusable(false);
            descriptionTextView.setFocusable(false);
            dateCreatedTextView.setFocusable(false);
            isAvailableCheckBox.setFocusable(false);
            isDiscardedCheckBox.setFocusable(false);
            productTypeRadioGroup.setFocusable(false);
            btnSave.setVisibility(View.GONE);
            btnCancel.setText("Back");
        }
    }

    private void savedProductObserver() {

        productsData.getSavedId().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {

                Toast.makeText(getApplication(), " item has been saved successfully ", Toast.LENGTH_LONG).show();

                Intent i = new Intent(ProductAddActivity.this, ProductsMainActivity.class);
                startActivity(i);
            }
        });
    }

    private void deleteObserver(){
        productsData.getDeletedId().observe(ProductAddActivity.this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(Objects.equals(aLong,productFromIntent.getId())) {

                    Toast.makeText(ProductAddActivity.this, "Product has been deleted !!!", Toast.LENGTH_LONG);

                    Intent i = new Intent(ProductAddActivity.this, ProductsMainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(ProductAddActivity.this, "Product could't be deleted !!!", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void btnCancelOnClick() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductAddActivity.this, ProductsMainActivity.class);
                startActivity(i);
            }
        });
    }

    private void btnSaveOnClick() {

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProduct();
            }
        });
    }

    private void saveProduct() {

        Product product = ValidateInputsIntoProduct();
        if (product != null) {
            productsData.save(product);
        }
    }

    private void dateCreatedTextViewOnClick() {
        dateCreatedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProductAddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void isDiscardedCheckBoxOnClick() {
        isDiscardedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CompoundButton) v).isChecked()){
                    yearsToDiscard_TextInputLayout.setVisibility(View.GONE);
                }
                else{
                    yearsToDiscard_TextInputLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void productTypeRadioGroupOnClick() {
        productTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(MARadioButton.isChecked()){
                    yearsToMAConvertion_TextInputLayout.setVisibility(View.GONE);
                    amortizationPercent_TextInputLayout.setVisibility(View.GONE);
                }else{
                    yearsToMAConvertion_TextInputLayout.setVisibility(View.VISIBLE);
                    amortizationPercent_TextInputLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Product ValidateInputsIntoProduct() {

        boolean valid=true;

        String productName=productNameTextView.getText().toString();
        if(productName.length()<1) {
            productNameTextView.setError("product name is required !!!");
            valid=false;
        }
        String inventoryNumberSTR=inventoryNumberTextView.getText().toString();
        if (inventoryNumberSTR.length()<1){
            inventoryNumberTextView.setError("inventory number is required !!!");
            valid=false;
        }
        String dateCreated=dateCreatedTextView.getText().toString();
        if(dateCreated.length()<1){
            dateCreatedTextView.setError("date created is required !!!" );
            valid=false;
        }
        String description=descriptionTextView.getText().toString();
        boolean isAvailable= isAvailableCheckBox.isChecked();
        boolean isDiscarded=isDiscardedCheckBox.isChecked();

        int yearsToDiscard = 0;
        if(!isDiscarded){
            String yearsToDiscaredSTR=yearsToDiscardTextView.getText().toString();
            if(yearsToDiscaredSTR.length()<1){
                yearsToDiscardTextView.setError("years to discared is required !!!");
                valid=false;
            }else{
                yearsToDiscard=Integer.parseInt(yearsToDiscaredSTR);
            }
        }

        ProductType productType;
        if(DMARadioButton.isChecked()){
            productType=ProductType.DMA;
        }else{
            productType=ProductType.MA;
        }

        int yearsToMAConvertion = 0,amortizationPercent=0;
        if(productType.equals(ProductType.DMA)){
            String yearsToMAConvertionSTR = yearsToMAConvertionTextView.getText().toString();
            if(yearsToMAConvertionSTR.length()<1){
                yearsToMAConvertionTextView.setError("years to MA conversion is reqired !!!");
                valid=false;
            }else{
                yearsToMAConvertion=Integer.parseInt(yearsToMAConvertionSTR);
            }
            String amortizationPercentSTR=amortizationPercentTextView.getText().toString();
            if(amortizationPercentSTR.length()<1){
                amortizationPercentTextView.setError("amortization percent is reqiured !!!");
                valid=false;
            }else{
                amortizationPercent=Integer.parseInt(amortizationPercentSTR);
            }
        }

        Product product = null;
        if (valid){
            product = new Product();
            if(productFromIntent!=null)
                product.setId(productFromIntent.getId());
            product.setName(productName);
            product.setInventoryNumber(inventoryNumberSTR);
            product.setDescription(description);
            product.setDateCreated( getDate(dateCreated));//ft.parse(dateCreated) );
            product.setAvailable(isAvailable);
            product.setDiscarded(isDiscarded);
            if(!isDiscarded)
                product.setYearsToDiscard(yearsToDiscard);

            product.setProductType(productType);
            if(productType==ProductType.DMA)
            {
                product.setAmortizationPercent(amortizationPercent);
                product.setYearsToMAConvertion(yearsToMAConvertion);
            }
        }
        return product;
    }

    private Date getDate(String dateCreated) {

        try {
            return ft.parse(dateCreated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.products_add_menu,menu);
        if(loggedUser.getRole().equals(Role.ROLE_Employee)){
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_delete_product:

                new AlertDialog.Builder(ProductAddActivity.this)
                        .setTitle("Delete Product").setMessage(" sure you want to delete "
                        + productFromIntent.getName()+" ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                productsData.deleteId(productFromIntent.getId());
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
                return true;
            case R.id.icon_edit_product:
                saveProduct();
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

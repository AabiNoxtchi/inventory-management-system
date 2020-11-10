package com.example.inventoryui.Activities.Products;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.HelperFilters.MinMaxValueFilter;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product;
import com.example.inventoryui.Models.ProductType;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.Models.UpdatedProductResponse;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        amortizationPercentTextView.setFilters( new InputFilter[]{ new MinMaxValueFilter( "1" , "99" )});
        dateCreatedTextView.setText(ft.format(new Date()));

        productsData= new ViewModelProvider(this).get(ProductsData.class);

        Intent i=getIntent();
        if(i.hasExtra("productForUpdate")) {
            productFromIntent = (Product) i.getSerializableExtra("productForUpdate");
            initializeFields();
        }

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
        insertProductObserver();
        updateProductObserver();
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

    private void initializeFields() {
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

    private void insertProductObserver() {
            productsData.getInsertedProduct().observe(this, new Observer<Boolean>(){
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean){
                        Toast.makeText(getApplication()," 1 product have been saved successfully ", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ProductAddActivity.this, ProductsMainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplication(),"error couldn't save product !!! ",Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    private void updateProductObserver() {
        productsData.getUpdatedProduct().observe(this, new Observer<UpdatedProductResponse>(){

            @Override
            public void onChanged(UpdatedProductResponse response) {
                if(response!=null){
                    if(response.isDiscarded()){
                        showMsg("Discarded product",response.getProductName()+" has been Discarded .");
                    }
                    if (response.isConvertedToMA()) {
                        showMsg("Discarded product",response.getProductName()+" has been Converted to MA .");
                    }
                    if(!response.isConvertedToMA()&&!response.isDiscarded()) {
                        Toast.makeText(getApplication(), response.getProductName()+" has been updated successfully ", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ProductAddActivity.this, ProductsMainActivity.class);
                        startActivity(i);
                    }
                }else{
                    Toast.makeText(getApplication(),"error couldn't update product !!! ",Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private void btnSaveOnClick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = null;
                try {
                    product = ValidateInputsIntoProduct();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (product != null) {
                    if (productFromIntent != null)
                        productsData.updateProduct(product, null);
                    else
                        productsData.insertProduct(product);
                }
            }
        });
    }

    private Product ValidateInputsIntoProduct() throws ParseException {
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

        int yearsToDiscared=0;
        if(!isDiscarded){
            String yearsToDiscaredSTR=yearsToDiscardTextView.getText().toString();
            if(yearsToDiscaredSTR.length()<1){
                yearsToDiscardTextView.setError("years to discared is required !!!");
                valid=false;
            }else{
                yearsToDiscared=Integer.parseInt(yearsToDiscaredSTR);
            }
        }

        ProductType productType;
        if(DMARadioButton.isChecked()){
            productType=ProductType.DMA;
        }else{
            productType=ProductType.MA;
        }

        int yearsToMAConvertion=0,amortizationPercent=0;
        if(productType.equals(ProductType.DMA)){
            String yearsToMAConvertionSTR=yearsToMAConvertionTextView.getText().toString();
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

        Product product=null;
        if (valid){
            product=new Product();
            if(productFromIntent!=null)
            product.setId(productFromIntent.getId());
            product.setName(productName);
            product.setInventoryNumber(inventoryNumberSTR);
            product.setDescription(description);
            product.setDateCreated( ft.parse(dateCreated) );
            product.setAvailable(isAvailable);
            product.setDiscarded(isDiscarded);
            if(!isDiscarded)
                product.setYearsToDiscard(yearsToDiscared);

            product.setProductType(productType);
            if(productType==ProductType.DMA)
            {
                product.setAmortizationPercent(amortizationPercent);
                product.setYearsToMAConvertion(yearsToMAConvertion);
            }
        }
        return product;
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

    public void showMsg(String title,String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title).setMessage(msg).setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(ProductAddActivity.this, ProductsMainActivity.class);
                        startActivity(i);
                    }
                }).show();
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

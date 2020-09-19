package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Product;
import com.example.inventoryui.Models.ProductType;
import com.example.inventoryui.Models.UpdatedProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProductsData extends AndroidViewModel {

    private MainRequestQueue mainRequestQueue;
    private Long userId;
    public ProductsData(@NonNull Application application) {
        super(application);

        mainRequestQueue = MainRequestQueue.getInstance(application);
        userId=((AuthenticationManager)this.getApplication()).getLoggedUser().getId();
    }


    private MutableLiveData<String> response;
    public MutableLiveData<String> getResponse(){
        if (response==null)
            response=new MutableLiveData<>();
        return response;
    }

    private MutableLiveData<ArrayList<Product>> products;
    private MutableLiveData<ArrayList<Product>> productsForEmployee;
    public MutableLiveData<ArrayList<Product>> getProductsForEmployee(Long employeeId){

        getAllProductsForUser(null,
                null, null, employeeId);
        if(productsForEmployee==null)
            productsForEmployee=new MutableLiveData<>();
        return productsForEmployee;
    }
    public void getProductsForUser(){
        getAllProductsForUser(null,null, null,null);
    }
    public void getProductByType(ProductType productType){
        getAllProductsForUser(productType,null, null,null);
    }
    public void getDiscardedProducts(Boolean discarded){
        getAllProductsForUser(null,discarded, null,null);

    }

    public void getAvailableProductsForUser(Boolean available){
        getAllProductsForUser(null,null,available,null);
    }

    public MutableLiveData<ArrayList<Product>> getAllProductsForUser(@Nullable ProductType productType,
                                                                     @Nullable Boolean discarded, Boolean available,@Nullable final Long employeeId){
        if(products==null)
            products=new MutableLiveData<>();

        String url ="http://192.168.1.2:8080/products/"+userId;
        if(productType!=null){
            url+="/"+productType;
        }else if(discarded!=null){
            url+="/discarded/"+discarded;
        }else if(available!=null){
            url+="/available/"+available;
        }else if(employeeId!=null){
            url+="/employee/"+employeeId;
        }

       // Toast.makeText(getApplication(),"user id"+userId, Toast.LENGTH_LONG).show();
        StringRequest productsForUserRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(getApplication(),response, Toast.LENGTH_LONG).show();
                getResponse().setValue(response);//--------------testing------------//

                ObjectMapper om=new ObjectMapper();
                ArrayList<Product> list = null;
                try {
                    list = om.readValue(response,new TypeReference<ArrayList<Product>>(){});

                } catch (IOException e) {
                    Toast.makeText(getApplication(),"exception parsing ", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if(employeeId!=null)
                {
                    productsForEmployee.setValue(list);
                }
                else {
                    products.setValue(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"error", Toast.LENGTH_LONG).show();
            }
        });
        mainRequestQueue.getRequestQueue().add(productsForUserRequest);
        return products;
    }



    private MutableLiveData<Boolean> insertedProduct;
    public MutableLiveData<Boolean> getInsertedProduct(){
        if(insertedProduct==null) {
            insertedProduct = new MutableLiveData<>();
        }
        return insertedProduct;
    }

    public void insertProduct(Product product) throws JSONException {

        String url ="http://192.168.1.2:8080/products/add/"+userId;

        SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");//"dd-MM-yyyy hh:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        JSONObject json = null;
        try {
             json=new JSONObject(mapper.writeValueAsString(product));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response ) {
                        getInsertedProduct().setValue(true);
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getInsertedProduct().setValue(false);
                    }
                });
        mainRequestQueue.getRequestQueue().add(jsonObjectRequest);
    }

    private MutableLiveData<UpdatedProductResponse> updatedProduct;
    public MutableLiveData<UpdatedProductResponse> getUpdatedProduct(){
        if(updatedProduct==null) {
            updatedProduct = new MutableLiveData<>();
        }
        return updatedProduct;
    }

    public void updateProduct(Product product,Long employeeId) throws JSONException {

        //UpdatedProductResponse updatedProductResponse=null;
        String url ="http://192.168.1.2:8080/products/"+userId;
        if(employeeId!=null){
            url+="/addemployee/"+employeeId;
        }

        SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");//"dd-MM-yyyy hh:mm");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        JSONObject json = null;
        try {
            json=new JSONObject(mapper.writeValueAsString(product));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest productUpdateJsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {
                        try {
                           getUpdatedProduct().setValue(mapper.readValue(response.toString(), UpdatedProductResponse.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //getUpdatedProduct().setValue(true);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getUpdatedProduct().setValue(null);
            }
        });
        mainRequestQueue.getRequestQueue().add(productUpdateJsonObjectRequest);
    }



}

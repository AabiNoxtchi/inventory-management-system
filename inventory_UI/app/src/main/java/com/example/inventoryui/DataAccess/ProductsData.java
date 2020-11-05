package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsData extends AndroidViewModel {

    private static final String TAG = "MyAcivity_ProductsData";

    private MainRequestQueue mainRequestQueue;
    private Long userId;
    private String authToken;
    ObjectMapper om;
    public ProductsData(@NonNull Application application) {
        super(application);

        this.mainRequestQueue = MainRequestQueue.getInstance(application);
        this.userId=((AuthenticationManager)this.getApplication()).getLoggedUser().getId();
        this.authToken=((AuthenticationManager)this.getApplication()).getAuthToken();
    }

    private MutableLiveData<String> response;
    public MutableLiveData<String> getResponse(){
        if (response==null)
            response=new MutableLiveData<>();
        return response;
    }

    private MutableLiveData<ArrayList<Product>> products;
    public MutableLiveData<ArrayList<Product>> getProducts(){
        if(products==null)
            products=new MutableLiveData<>();
        return products;
    }
   /* private MutableLiveData<ArrayList<Product>> productsForEmployee;
    public MutableLiveData<ArrayList<Product>> getProductsForEmployee(Long employeeId){

        getAllProductsForUser(null,
                null, null, employeeId);
        if(productsForEmployee==null)
            productsForEmployee=new MutableLiveData<>();
        return productsForEmployee;
    }*/
    public void getProductsForEmployee(Long employeeId){
       getAllProductsForUser(null,null, null,employeeId, null);
   }
    public void getProductsForUser(){
        getAllProductsForUser(null,null, null,null, null);
    }
    public void getProductByType(ProductType productType){
        getAllProductsForUser(productType,null, null,null, null);
    }
    public void getDiscardedProducts(Boolean discarded){
        getAllProductsForUser(null,discarded, null,null, null);

    }
    public void getAvailableProductsForUser(Boolean available){
        getAllProductsForUser(null,null,available,null, null);
    }


    public MutableLiveData<ArrayList<Product>> getAllProductsForUser(@Nullable ProductType productType,
                                                                     Boolean discarded,
                                                                     Boolean available,
                                                                     @Nullable final Long employeeId,
                                                                     String productIdsFromIntent){
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
        }else if(productIdsFromIntent!=null){
            url=addToUrl(productIdsFromIntent,url);
        }
        StringRequest productsForUserRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                getResponse().setValue(response);//--------------testing------------//

               if(om==null)om=new ObjectMapper();
                ArrayList<Product> list = null;
                try {
                    list = om.readValue(response,new TypeReference<ArrayList<Product>>(){});

                } catch (IOException e) {
                    e.printStackTrace();
                }
                    products.setValue(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap();
                String token="Bearer "+ authToken;
                map.put("Authorization", token);

                return map;
            }
        };
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
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap();
                String token="Bearer "+ authToken;
                map.put("Authorization", token);
                return map;
            }
        };
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
                            UpdatedProductResponse updatedProduct=mapper.readValue(response.toString(), UpdatedProductResponse.class);
                            getUpdatedProduct().setValue(updatedProduct);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getUpdatedProduct().setValue(null);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap();
                String token="Bearer "+ authToken;
                map.put("Authorization", token);
                return map;
            }
        };
        mainRequestQueue.getRequestQueue().add(productUpdateJsonObjectRequest);
    }

    private String addToUrl(String list, String url){
        String ids=list.substring(1, list.length() - 1);
        url+="/ids/"+ids;
         return url;
    }

    private void showError(VolleyError error){

        try {
            String responseError=new String(error.networkResponse.data,"utf-8");
            JSONObject data=new JSONObject(responseError);
            String msg=data.optString("message");
            if(msg.equals("Error: Unauthorized")) ((AuthenticationManager)this.getApplication()).logout();
               Toast.makeText(getApplication(),msg, Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}

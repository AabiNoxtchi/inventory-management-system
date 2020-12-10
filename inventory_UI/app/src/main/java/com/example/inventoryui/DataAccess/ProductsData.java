package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Product.SelectProduct;
import com.example.inventoryui.Models.Product.Selectable;
import com.example.inventoryui.Utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductsData extends BaseData<Product, IndexVM> {

    public ProductsData(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Class getIndexVMClass() {
        return com.example.inventoryui.Models.Product.IndexVM.class;
    }

    @Override
    protected Class EClass() {
        return Product.class ;
    }

    @Override
    String addToUrl() {
        return "/products" ;
    }


    private MutableLiveData<Selectable> freeProducts ;
    public MutableLiveData<Selectable> getFreeProducts() {

        if(freeProducts==null)
            freeProducts=new MutableLiveData<>();
        //String url = this.url +"?Filter.freeProducts=true&Filter.filtersSet=true";
        String url = this.url +"/selectProducts";

        StringRequest freeProductsRequest =
                new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.i(TAG,"freeProducts = "+response);
                                //List<SelectProduct> items = getSelectProductList(response);
                                //freeProducts.setValue(items);
                                freeProducts.setValue((Selectable) getType(response, Selectable.class));

                            }
                        }, errorListener())
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        return getHeaderMap();
                    }
                };
        mainRequestQueue.getRequestQueue().add(freeProductsRequest);
        return freeProducts;
    }

   /* private MutableLiveData<List<Product>> freeProducts ;
    public MutableLiveData<List<Product>> getFreeProducts() {
        if(freeProducts==null)
            freeProducts=new MutableLiveData<>();

        //String url = this.url +"?Filter.freeProducts=true&Filter.filtersSet=true";

        StringRequest freeProductsRequest =
                new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                freeProducts.setValue(((IndexVM) getType(response, getIndexVMClass())).getItems());

                            }
                        }, errorListener())
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        return getHeaderMap();
                    }
                };
        mainRequestQueue.getRequestQueue().add(freeProductsRequest);
        return freeProducts;
    }*/

    private MutableLiveData<List<Long>> nullifiedIds ;
    public MutableLiveData<List<Long>> getNullifiedIds() {
        if(nullifiedIds==null)
            nullifiedIds=new MutableLiveData<>();
        return nullifiedIds;
    }

    public void nullifyIds(List<Long> ids){

        String listToString = Utils.ListStringToUrlString(ids.toString());
        String url = this.url +"/nullify/"+listToString;//+ idsUrl(ids);

        StringRequest nullifyIdsRequest =
                new StringRequest(
                        Request.Method.DELETE,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                getNullifiedIds().setValue(getListLong(response));

                            }
                        }, errorListener())
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        return getHeaderMap();
                    }
                };
        mainRequestQueue.getRequestQueue().add(nullifyIdsRequest);

    }

    private MutableLiveData<Selectable> filledIds;
    public MutableLiveData<Selectable> getFilledIds(){
        if(filledIds == null) {
            filledIds = new MutableLiveData<>();
        }
        return filledIds;
    }
    public void fillIds(Selectable selectable ){

        String url = this.url + "/fillIds";
        JsonObjectRequest fillIdsRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                getJsonObject(selectable),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {

                        Log.i(TAG,"respone = "+response);
                        getFilledIds().setValue((Selectable) getType(response.toString(),Selectable.class));
                    }
                },errorListener()){
            @Override
            public Map<String, String> getHeaders(){
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(fillIdsRequest);
    }

    private ArrayList<SelectProduct> getSelectProductList(String response) {
        final ObjectMapper mapper = new ObjectMapper();
        ArrayList<SelectProduct> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<SelectProduct>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}

package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Product.SelectProduct;
import com.example.inventoryui.Utils.Utils;

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


    private MutableLiveData<ArrayList<SelectProduct>> freeProducts ;
    public MutableLiveData<ArrayList<SelectProduct>> getFreeProducts() {
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

                                freeProducts.setValue(getList(response));

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

    private MutableLiveData<ArrayList<Long>> nullifiedIds ;
    public MutableLiveData<ArrayList<Long>> getNullifiedIds() {
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

                                getNullifiedIds().setValue(getList(response));

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

}

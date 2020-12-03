package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Utils.Utils;

import java.util.ArrayList;
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


    private MutableLiveData<ArrayList<Long>> nullifiedIds ;
    public MutableLiveData<ArrayList<Long>> getNullifiedIds() {
        if(nullifiedIds==null)
            nullifiedIds=new MutableLiveData<>();
        return nullifiedIds;
    }

    public void nullifyIds(ArrayList<Long> ids){

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

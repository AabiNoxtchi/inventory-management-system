package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.BaseIndexVM;
import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Utils.Utils;
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
import java.util.List;
import java.util.Map;

public abstract class BaseData<E extends BaseModel, IndexVM extends BaseIndexVM> extends AndroidViewModel {

    public static final String TAG = BaseData.class.getSimpleName();


    private String authToken;
    private ObjectMapper mapper = new ObjectMapper();
    private SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");

    protected String url ;
    protected MainRequestQueue mainRequestQueue;

    protected abstract Class getIndexVMClass();
    protected abstract Class EClass();
    abstract String addToUrl();

    public BaseData(@NonNull Application application) {
        super(application);

        this.mainRequestQueue = MainRequestQueue.getInstance(application);
       // this.url = MainRequestQueue.BASE_URL + addToUrl() ;
        this.url = ((AuthenticationManager)this.getApplication()).BASE_URL + addToUrl();
        this.authToken=((AuthenticationManager)this.getApplication()).getAuthToken();

        this.mapper.setDateFormat(df);
    }

    private MutableLiveData<IndexVM> IndexVM;
    public MutableLiveData<IndexVM> getIndexVM() {
        if(IndexVM==null)
            IndexVM=new MutableLiveData<>();
        return IndexVM;
    }
    public void getAll(IndexVM model){

        String url =this.url;
        if(model != null ) url = url + model.getUrl();

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getIndexVM().setValue( (IndexVM) getType(response, getIndexVMClass()) );
            }
        }, errorListener())
        {
            @Override
            public Map<String, String> getHeaders()  {
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(productsRequest);
    }


    private MutableLiveData<E> savedId;
    public MutableLiveData<E> getSavedId(){
        if(savedId == null) {
            savedId = new MutableLiveData<>();
        }
        return savedId;
    }
    public void save(E item ){

        String url = this.url + "/save";
       /* if(employeeId!=null){
            url+="/"+employeeId;
        }*/
        JsonObjectRequest saveRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                getJsonObject(item),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {

                        Log.i(TAG," response = "+response.toString());


                       getSavedId().setValue((E)getType(response.toString(), EClass()));
                        /*UpdatedProductResponse updatedProduct =
                                (UpdatedProductResponse)getType(response.toString(), UpdatedProductResponse.class);
                        if(updatedProduct.getResponse().equals("saved"))
                            getInsertedProduct().setValue(true);
                        else
                            getUpdatedProduct().setValue(updatedProduct);*/
                    }
                },errorListener()){
            @Override
            public Map<String, String> getHeaders(){
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(saveRequest);
    }



    private MutableLiveData<ArrayList<Long>> deletedIds ;
    public MutableLiveData<ArrayList<Long>> getDeletedIds() {
        if(deletedIds==null)
            deletedIds=new MutableLiveData<>();
        return deletedIds;
    }
    private MutableLiveData<Long> deletedId ;
    public MutableLiveData<Long> getDeletedId() {
        if(deletedId==null)
            deletedId=new MutableLiveData<>();
        return deletedId;
    }

    public void deleteId(Long id){
        String idsUrl = "/id/"+id;
        deleteAll(idsUrl);
    }
    public void deleteIds(List<Long> ids){

        String listToString = Utils.ListStringToUrlString(ids.toString());
        String idsUrl = "/ids/"+listToString;
        deleteAll(idsUrl);
    }
    private void deleteAll(String idsUrl){

        String url = this.url + idsUrl;

        StringRequest deleteAllRequest =
                new StringRequest(
                        Request.Method.DELETE,
                        url,
                        new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(url.contains("ids"))
                        getDeletedIds().setValue(getList(response));
                    else
                        getDeletedId().setValue((Long)getType(response,Long.class));
                }
        }, errorListener())
        {
            @Override
            public Map<String, String> getHeaders() {
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(deleteAllRequest);

    }


    protected Object getType(String from, Class to){
        Object o = null;
        try {
            o = mapper.readValue(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

    protected ArrayList<Long> getList(String response) {
        ArrayList<Long> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<Long>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    protected JSONObject getJsonObject(Object object){
        JSONObject json = null;
        try {
            json = new JSONObject(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected Map<String, String> getHeaderMap() {

        HashMap<String, String> map = new HashMap();
        map.put("Authorization", "Bearer "+ authToken);
        return map;
    }

    protected Response.ErrorListener errorListener(){
        return
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError(error);
                    }
                };
    }

    protected void showError(VolleyError error){

        if (error instanceof NetworkError) {
            Log.i(TAG, "net work error !!!");
            Toast.makeText(getApplication(),"net work error !!!", Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Log.i(TAG,error.toString());
            Toast.makeText(getApplication(),"error ,please try out later !!!", Toast.LENGTH_LONG).show();
        }else {
            try {
                Log.i(TAG,error.toString());

                String responseError=new String(error.networkResponse.data,"utf-8");
                JSONObject data=new JSONObject(responseError);
                String msg = data.optString("message");
                Log.i(TAG,msg);

                if(msg.equals("Error: Unauthorized")) ((AuthenticationManager)this.getApplication()).logout();
                Toast.makeText(getApplication(),msg, Toast.LENGTH_LONG).show();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

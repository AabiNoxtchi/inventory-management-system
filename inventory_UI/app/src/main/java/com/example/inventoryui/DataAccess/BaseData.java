package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.BaseIndexVM;
import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseData<E extends BaseModel, IndexVM extends BaseIndexVM> extends AndroidViewModel {

    public static final String TAG = BaseData.class.getSimpleName();


    protected String authToken;

    protected String url ;
    protected MainRequestQueue mainRequestQueue;

    protected abstract Class getIndexVMClass();
    protected abstract Class EClass();
    abstract String addToUrl();

    public BaseData(@NonNull Application application) {
        super(application);

        this.mainRequestQueue = MainRequestQueue.getInstance(application);
        this.url = ((AuthenticationManager)this.getApplication()).BASE_URL + addToUrl();
        this.authToken=((AuthenticationManager)this.getApplication()).getAuthToken();

    }

    private MutableLiveData<E> item;
    public MutableLiveData<E> getItem() {
        if(item==null)
            item=new MutableLiveData<>();
        return item;
    }
    public void getById(Long id){

        String url =this.url+"/"+id ;
        Log.i(TAG, "url = "+url);

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getItem().setValue( (E) getType(response, EClass()) );
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
        JsonObjectRequest saveRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                getJsonObject(item),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {

                       getSavedId().setValue((E)getType(response.toString(), EClass()));
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
        return mainRequestQueue.getType(from, to);
    }

    protected ArrayList<Long> getList(String response) {
       return mainRequestQueue.getList(response);
    }

    protected JSONObject getJsonObject(Object object){
        return mainRequestQueue.getJsonObject(object);
    }

    protected Map<String, String> getHeaderMap() {

        HashMap<String, String> map = new HashMap();
        map.put("Authorization", "Bearer "+ authToken);
        return map;
    }

    protected Response.ErrorListener errorListener(){
        return mainRequestQueue.errorListener();
    }

}

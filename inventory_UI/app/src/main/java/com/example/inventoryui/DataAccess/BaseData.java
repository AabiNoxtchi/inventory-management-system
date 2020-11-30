package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.BaseIndexVM;
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

public abstract class BaseData</*E extends BaseModel,*/ IndexVM extends BaseIndexVM> extends AndroidViewModel {

    private static final String TAG = "MainData";
   // final static String TAG="MyActivity_baseMain";
   protected MainRequestQueue mainRequestQueue;
    private String authToken;
    private ObjectMapper mapper = new ObjectMapper();
    private SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");

    protected String url ;
    protected abstract Class getIndexVMClass();

    abstract String addToUrl();

    public BaseData(@NonNull Application application) {
        super(application);
        this.mainRequestQueue = MainRequestQueue.getInstance(application);
        this.url = MainRequestQueue.BASE_URL + addToUrl() ;//+ "/products";
        this.authToken=((AuthenticationManager)this.getApplication()).getAuthToken();
        mapper.setDateFormat(df);
        Log.i(TAG,"this class = "+this.getClass().getName());
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
        Log.i(TAG,"url = "+url);

        StringRequest productsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"IndexVM class = "+getIndexVMClass().getName());
                getIndexVM().setValue( (IndexVM) getType(response, getIndexVMClass()) );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(productsRequest);
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

       // String listToString = Utils.ListStringToUrlString(ids.toString());
        String url = this.url + idsUrl;
        Log.i(TAG,"url = "+ url);

        StringRequest deleteAllRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "deleted ids response = "+response);
                //ArrayList<Long> deletedIds = ;
                if(response.contains(","))
                    getDeletedIds().setValue(getList(response));
                else
                    getDeletedId().setValue((Long)getType(response,Long.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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

   /* protected List<Long> getList(String response) {
        List<Long> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<Long>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }*/

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
            json=new JSONObject(mapper.writeValueAsString(object));
            Log.i(TAG,"product obj = "+json);
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

    protected void showError(VolleyError error){
        if (error instanceof NetworkError) {
            Log.i(TAG, "net work error !!!");
            Toast.makeText(getApplication(),"net work error !!!", Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Log.i(TAG,error.toString());
            Toast.makeText(getApplication(),error.toString(), Toast.LENGTH_LONG).show();
        }else {
            try {

                Log.i(TAG,error.toString());
                String responseError=new String(error.networkResponse.data,"utf-8");
                JSONObject data=new JSONObject(responseError);
                String msg=data.optString("message");
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

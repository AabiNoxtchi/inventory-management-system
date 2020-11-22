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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseData<IndexVM extends BaseIndexVM> extends AndroidViewModel {

    private static final String TAG = "MyActivity_MainData";
   // final static String TAG="MyActivity_baseMain";
    private MainRequestQueue mainRequestQueue;
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
        //return IndexVM;
    }



    private Object getType(String from, Class to){
        Object o = null;
        try {
            o = mapper.readValue(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

    private JSONObject getJsonObject(Object object){
        JSONObject json = null;
        try {
            json=new JSONObject(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private Map<String, String> getHeaderMap() {
        HashMap<String, String> map = new HashMap();
        map.put("Authorization", "Bearer "+ authToken);
        return map;
    }

    private void showError(VolleyError error){
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

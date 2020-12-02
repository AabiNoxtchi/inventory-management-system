package com.example.inventoryui.DataAccess;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.inventoryui.Models.AuthenticationManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainRequestQueue {

    public static final String TAG = MainRequestQueue.class.getSimpleName();

    private static MainRequestQueue instance;
    private Context context;
    private RequestQueue requestQueue;

    final ObjectMapper mapper = new ObjectMapper();
  //  protected ObjectMapper mapper = new ObjectMapper();
    protected SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");
    //192.168.1.12
    //public static final String BASE_URL = "http://192.168.1.2:8080/api/inventory";

    private MainRequestQueue(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
        this.mapper.setDateFormat(df);

    }

    public static synchronized MainRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new MainRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public Object getType(String from, Class to){
        Object o = null;
        try {
            o = mapper.readValue(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

    public ArrayList<Long> getList(String response) {
        ArrayList<Long> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<Long>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public JSONObject getJsonObject(Object object){
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

    public Response.ErrorListener errorListener(){
        return
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError(error);
                    }
                };
    }

    public void showError(VolleyError error){

        if (error instanceof NetworkError) {
            Log.i(TAG, "net work error !!!");
            Toast.makeText(context,"net work error !!!", Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Log.i(TAG,error.toString());
            Toast.makeText(context,"error ,please try out later !!!", Toast.LENGTH_LONG).show();
        }else {
            try {
                Log.i(TAG,error.toString());

                String responseError=new String(error.networkResponse.data,"utf-8");
                JSONObject data=new JSONObject(responseError);
                String msg = data.optString("message");
                Log.i(TAG,msg);

                if(msg.equals("Error: Unauthorized")) ((AuthenticationManager)this.context).logout();
                Toast.makeText(context,msg, Toast.LENGTH_LONG).show();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}

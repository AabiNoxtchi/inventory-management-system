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
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LoginRequest;
import com.example.inventoryui.Models.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginData extends AndroidViewModel {
    private static final String TAG = "MyActivity_LoginData";
    private MainRequestQueue mainRequestQueue;
    private String url ;
    private String authToken;
    final ObjectMapper mapper = new ObjectMapper();

    public LoginData(@NonNull Application application) {
        super(application);
        mainRequestQueue = MainRequestQueue.getInstance(application);
        this.url =  MainRequestQueue.BASE_URL + "/auth";
        authToken=((AuthenticationManager)this.getApplication()).getAuthToken();
    }

    private MutableLiveData<LoginResponse> loggedUser ;
    public MutableLiveData<LoginResponse> getLoggedUser() {
        if (loggedUser== null) {
            loggedUser = new MutableLiveData<>();
        }
        return loggedUser;
    }
    public void getLoggedUser(LoginRequest loginRequested){
        //192.168.1.3
        //192.168.1.2
        String url = this.url+"/signin";
        JSONObject json=getJsonObject(loginRequested);

        JsonObjectRequest loginJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {
                        // LoginResponse loginResponse = getLoginResponse(response);
                        getLoggedUser().setValue(
                                (LoginResponse)getType(response.toString(), LoginResponse.class));
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
                // getLoggedUser().setValue(null);
            }
        });
        mainRequestQueue.getRequestQueue().add(loginJsonObjectRequest);
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

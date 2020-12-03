package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LogInRegister.LoginRequest;
import com.example.inventoryui.Models.LogInRegister.LoginResponse;

import org.json.JSONObject;

public class LoginData extends AndroidViewModel {
    private static final String TAG = "MyActivity_LoginData";
    private MainRequestQueue mainRequestQueue;
    private String url ;
    private String authToken;

    public LoginData(@NonNull Application application) {
        super(application);
        mainRequestQueue = MainRequestQueue.getInstance(application);
        this.url =  ((AuthenticationManager)this.getApplication()).BASE_URL + "/auth";
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
        String url = this.url+"/signin";
        JSONObject json=getJsonObject(loginRequested);
        JsonObjectRequest loginJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {
                        getLoggedUser().setValue(
                                (LoginResponse)getType(response.toString(), LoginResponse.class));
                    }
                },
                errorListener()
           );
        mainRequestQueue.getRequestQueue().add(loginJsonObjectRequest);
    }

    private Object getType(String from, Class to){

        return mainRequestQueue.getType(from, to);
    }

    private JSONObject getJsonObject(Object object){

        return mainRequestQueue.getJsonObject(object);
    }

    protected Response.ErrorListener errorListener(){

        return mainRequestQueue.errorListener();
    }

}

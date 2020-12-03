package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.inventoryui.Models.LogInRegister.RegisterRequest;
import com.example.inventoryui.Models.LogInRegister.RegisterResponse;
import com.example.inventoryui.Models.User.User;

import org.json.JSONObject;

import java.util.Map;

public class UsersData extends BaseData<User, com.example.inventoryui.Models.User.IndexVM>{

    @Override
    protected Class getIndexVMClass() {
        return com.example.inventoryui.Models.User.IndexVM.class ;
    }

    @Override
    protected Class EClass() {
        return User.class ;
    }

    @Override
    String addToUrl() {
        return "/users" ;
    }

    public UsersData(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<RegisterResponse> insertedUser;
    public MutableLiveData<RegisterResponse> getInsertedUser(){
        if(insertedUser==null) {
            insertedUser = new MutableLiveData<>();
        }
        return insertedUser;
    }
    public void insertUser(final RegisterRequest registeredRequest) {
        String url = this.url+"/signup";
        JSONObject json = getJsonObject(registeredRequest);

        JsonObjectRequest registerJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {
                        getInsertedUser().setValue(
                                (RegisterResponse)getType( response.toString(),RegisterResponse.class ) );
                      }
                },errorListener())
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(registerJsonObjectRequest);
    }

}

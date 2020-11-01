package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LoginRequest;
import com.example.inventoryui.Models.LoginResponse;
import com.example.inventoryui.Models.RegisterRequest;
import com.example.inventoryui.Models.RegisterResponse;
import com.example.inventoryui.Models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersData extends AndroidViewModel {

    private MainRequestQueue mainRequestQueue;
    private String authToken;
    //private User currentLoggedUser;

    final ObjectMapper mapper = new ObjectMapper();

    public UsersData(@NonNull Application application) {
        super(application);

        mainRequestQueue = MainRequestQueue.getInstance(application);
        authToken=((AuthenticationManager)this.getApplication()).getAuthToken();
        //currentLoggedUser=((AuthenticationManager)this.getApplication()).getLoggedUser();
    }

    private MutableLiveData<LoginResponse> loggedUser ;
    public MutableLiveData<LoginResponse> getLoggedUser() {
        if (loggedUser== null) {
            loggedUser = new MutableLiveData<>();
        }
        return loggedUser;
    }
    public void getLoggedUser(LoginRequest loginRequested){

        JSONObject json = null;

        try {
            json=new JSONObject(mapper.writeValueAsString(loginRequested));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url ="http://192.168.1.2:8080/api/auth/signin";

        JsonObjectRequest loginJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response ) {

                        LoginResponse loginResponse=null;
                        if(response.length()>0) {

                            try {
                                loginResponse = mapper.readValue(response.toString(), LoginResponse.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        getLoggedUser().setValue(loginResponse);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
                getLoggedUser().setValue(null);
            }
        });
        mainRequestQueue.getRequestQueue().add(loginJsonObjectRequest);
    }

    private MutableLiveData<RegisterResponse> insertedUser;
    public MutableLiveData<RegisterResponse> getInsertedUser(){
        if(insertedUser==null) {
            insertedUser = new MutableLiveData<>();
        }
        return insertedUser;
    }
    public void insertUser(final RegisterRequest registeredRequest) {

        JSONObject json = null;

        try {
            json=new JSONObject(mapper.writeValueAsString(registeredRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplication(),json.toString(), Toast.LENGTH_LONG).show();

        String url ="http://192.168.1.2:8080/users/signup";

        JsonObjectRequest registerJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response ) {

                       // RegisterResponse registerResponse=null;
                        RegisterResponse registerResponse = null;
                       if(response.length()>0) {

                            try {
                                // not used for now will see if user changes his account will need it !!!
                                //Toast.makeText(getApplication(),response.toString(), Toast.LENGTH_LONG).show();
                                registerResponse = mapper.readValue(response.toString(), RegisterResponse.class);
                                getInsertedUser().setValue(registerResponse);
                               // boolean ig=registerResponse.isRefreshToken();
                               // Toast.makeText(getApplication(), " "+ig,Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }else     getInsertedUser().setValue(registerResponse);
                        //getInsertedUser().setValue(registerResponse);
                      }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization","Bearer "+authToken);

                return map;
            }
        };
        mainRequestQueue.getRequestQueue().add(registerJsonObjectRequest);
    }


    private MutableLiveData<ArrayList<User>> users;
    public MutableLiveData<ArrayList<User>> getAllUsers(){
        if(users==null)
            users=new MutableLiveData<>();

        String url ="http://192.168.1.2:8080/users";
        StringRequest allUsersRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Toast.makeText(getApplication(),"response list users "+response, Toast.LENGTH_LONG).show();
                ArrayList<User> list = null;
                try {
                    list = mapper.readValue(response,new TypeReference<ArrayList<User>>(){});
                    //Toast.makeText(getApplication(),"users.size = "+list.size(), Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                users.setValue(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
                //Toast.makeText(getApplication(),"error", Toast.LENGTH_LONG).show();
            }
        }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap();
                    String token="Bearer "+ authToken;
                    map.put("Authorization", token);

                    return map;
                }
        };
        mainRequestQueue.getRequestQueue().add(allUsersRequest);

        return users;
    }

    private void showError(VolleyError error){

        try {
            String responseError=new String(error.networkResponse.data,"utf-8");
            JSONObject data=new JSONObject(responseError);
            String msg=data.optString("message");
            if(msg.equals("Error: Unauthorized")) ((AuthenticationManager)this.getApplication()).logout();
            Toast.makeText(getApplication(),msg, Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

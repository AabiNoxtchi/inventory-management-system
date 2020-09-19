package com.example.inventoryui.DataAccess;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersData extends AndroidViewModel {

    private MainRequestQueue mainRequestQueue;
    public UsersData(@NonNull Application application) {
        super(application);

        mainRequestQueue = MainRequestQueue.getInstance(application);
    }

    private MutableLiveData<User> loggedUser ;
    public MutableLiveData<User> getLoggedUser() {
        if (loggedUser== null) {
            loggedUser = new MutableLiveData<>();
        }
        return loggedUser;
    }
    public void getByUserNameAndPassword(final String userName, final String password){

        String url ="http://192.168.1.2:8080/users/login";
        StringRequest loginRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                       // Toast.makeText(getApplication(), "response : "+response+"response length :  " +response.length(),
                         //      Toast.LENGTH_LONG).show();
                            User user=null;
                            if(response.length()>0) {
                                ObjectMapper om=new ObjectMapper();
                                try {
                                    user = om.readValue(response, User.class);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            getLoggedUser().setValue(user);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getLoggedUser().setValue(null);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",userName);
                params.put("password", password);
                return params;
            }
        };
        mainRequestQueue.getRequestQueue().add(loginRequest);
    }

    private MutableLiveData<Boolean> insertedUser;
    public MutableLiveData<Boolean> getInsertedUser(){
        if(insertedUser==null) {
            insertedUser = new MutableLiveData<>();
        }
        return insertedUser;
    }
    public void insertUser(final User user) {

        String url ="http://192.168.1.2:8080/users/add";

        StringRequest insertUserRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                       // Toast.makeText(getApplication(),response,Toast.LENGTH_LONG).show();
                        getInsertedUser().setValue(true);

                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getInsertedUser().setValue(false);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> obj = new HashMap<String, String>();
               // obj.put("id",user.getId().toString());
                obj.put("username",user.getUserName());
                obj.put("password",user.getPassword());
                obj.put("role",user.getRole().toString());
                return obj;
            }
        };

        mainRequestQueue.getRequestQueue().add(insertUserRequest);
    }

    private MutableLiveData<ArrayList<User>> users;
    public MutableLiveData<ArrayList<User>> getAllUsers(){
        if(users==null)
            users=new MutableLiveData<>();

        String url ="http://192.168.1.2:8080/users";
        StringRequest allUsersRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //textView.setText(response.toString());


               // Type arrayListType = new Type<ArrayList<User>>(){}.getType();
                ObjectMapper om=new ObjectMapper();
               /* try {
                    List<User> list = om.readValue(response,new TypeReference<List<User>>(){});
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                ArrayList<User> list = null;
                try {
                    list = om.readValue(response,new TypeReference<ArrayList<User>>(){});
                   // Toast.makeText(getApplication(),response+list.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                users.setValue(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"error", Toast.LENGTH_LONG).show();
            }
        });
        mainRequestQueue.getRequestQueue().add(allUsersRequest);

        return users;
    }

}

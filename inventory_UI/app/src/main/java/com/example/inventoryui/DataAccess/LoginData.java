package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
                },
                errorListener()
                /*new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
                // getLoggedUser().setValue(null);
            }
        }*/);
        mainRequestQueue.getRequestQueue().add(loginJsonObjectRequest);
    }

    private Object getType(String from, Class to){
        return mainRequestQueue.getType(from, to);
        /*Object o = null;
        try {
            o = mapper.readValue(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;*/
    }

    private JSONObject getJsonObject(Object object){
        return mainRequestQueue.getJsonObject(object);
        /*JSONObject json = null;
        try {
            json=new JSONObject(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;*/
    }

    protected Response.ErrorListener errorListener(){
        return mainRequestQueue.errorListener();
        /*return
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showError(error);
                    }
                };*/
    }



    private void showError(VolleyError error){
        mainRequestQueue.showError(error);
       /* if (error instanceof NetworkError) {
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
        }*/

    }

}

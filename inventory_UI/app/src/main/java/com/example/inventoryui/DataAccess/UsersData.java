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
import com.example.inventoryui.Models.User.UserIndexVM;

import org.json.JSONObject;

import java.util.Map;

public class UsersData extends BaseData<User, UserIndexVM>{ //AndroidViewModel {

   /* private static final String TAG = "MyActivity_UsersData";
    private MainRequestQueue mainRequestQueue;
    private String url ;
    private String authToken;
    final ObjectMapper mapper = new ObjectMapper();*/

    @Override
    protected Class getIndexVMClass() {
        return UserIndexVM.class ;
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
       /* mainRequestQueue = MainRequestQueue.getInstance(application);
        this.url = ((AuthenticationManager)this.getApplication()).BASE_URL + "/users";
        authToken=((AuthenticationManager)this.getApplication()).getAuthToken();*/
    }

  /*  private MutableLiveData<LoginResponse> loggedUser ;
    public MutableLiveData<LoginResponse> getLoggedUser() {
        if (loggedUser== null) {
            loggedUser = new MutableLiveData<>();
        }
        return loggedUser;
    }
    public void getLoggedUser(LoginRequest loginRequested){
        //192.168.1.3
        //192.168.1.2
        String url ="http://192.168.1.2:8080/api/auth/signin";
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
    }*/

    private MutableLiveData<RegisterResponse> insertedUser;
    public MutableLiveData<RegisterResponse> getInsertedUser(){
        if(insertedUser==null) {
            insertedUser = new MutableLiveData<>();
        }
        return insertedUser;
    }
    public void insertUser(final RegisterRequest registeredRequest) {
       // Log.i(TAG,"user data token = "+ authToken);
        String url = this.url+"/signup";

        JSONObject json=getJsonObject(registeredRequest);

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

   /* private MutableLiveData<ArrayList<User>> users;
    public MutableLiveData<ArrayList<User>> getAllUsers(){
        String url =this.url;
        if(users==null)
            users=new MutableLiveData<>();
        StringRequest allUsersRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                users.setValue( getList(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError(error);
            }
        }){@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeaderMap();
            }
        };
        mainRequestQueue.getRequestQueue().add(allUsersRequest);
        return users;
    }*/

   /* private ArrayList<User> getList(String response) {
        ArrayList<User> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<User>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
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

    }*/

}

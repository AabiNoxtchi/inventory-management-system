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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.User.Employee;
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

public class EmployeesData extends AndroidViewModel {

    private static final String TAG = "MyActivity_EmployeeData";
    private MainRequestQueue mainRequestQueue;
    private String url ;
    private Long userId;
    private String authToken;
    private ObjectMapper mapper = new ObjectMapper();

    public EmployeesData(@NonNull Application application) {
        super(application);
        this.mainRequestQueue = MainRequestQueue.getInstance(application);
        this.url = ((AuthenticationManager)this.getApplication()).BASE_URL + "/employees";
        this.userId=((AuthenticationManager)this.getApplication()).getLoggedUser().getId();
        this.authToken=((AuthenticationManager)this.getApplication()).getAuthToken();
    }

    private MutableLiveData<ArrayList<Employee>> employees;
    public MutableLiveData<ArrayList<Employee>> getAllEmployeesForUser(){
        if(employees==null)
            employees=new MutableLiveData<>();
        String url =this.url+"/"+userId;
        StringRequest employeesForUserRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Employee> list = getList(response);
                employees.setValue(list);
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
        mainRequestQueue.getRequestQueue().add(employeesForUserRequest);
        return employees;
    }

    private MutableLiveData<Employee> employeeById;
    public MutableLiveData<Employee> getEmployeeById(){
        if(employeeById==null) {
            employeeById = new MutableLiveData<>();
        }
        return employeeById;
    }
    public void getEmployeeById(long id){
        String url = this.url + "/employee/"+id;
        StringRequest employeeByIdRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getEmployeeById().setValue( (Employee)getType(response, Employee.class));
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
        mainRequestQueue.getRequestQueue().add(employeeByIdRequest);
    }

    private MutableLiveData<Boolean> insertedEmployee;
    public MutableLiveData<Boolean> getInsertedEmployee(){
        if(insertedEmployee==null) {
            insertedEmployee = new MutableLiveData<>();
        }
        return insertedEmployee;
    }
    public void insertEmployee(Employee employee){
        String url = this.url + "/add/"+userId;
        JsonObjectRequest insertEmployeeRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                getJsonObject(employee),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {
                        getInsertedEmployee().setValue(true);
                    }
                },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError(error);
                    getInsertedEmployee().setValue(false);
            }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                   return  getHeaderMap();
                }
            };
        mainRequestQueue.getRequestQueue().add(insertEmployeeRequest);
    }

    private MutableLiveData<Boolean> updatedEmployee;
    public MutableLiveData<Boolean> getUpdatedEmployee(){
        if(updatedEmployee==null) {
            updatedEmployee = new MutableLiveData<>();
        }
        return updatedEmployee;
    }
    public void updateEmployee(Employee employee){
        String url = this.url + "/"+userId;
        JsonObjectRequest employeeUpdateRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                getJsonObject(employee),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response ) {
                        getUpdatedEmployee().setValue(true);
                    }
                },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError(error);
                    getUpdatedEmployee().setValue(null);
            }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getHeaderMap();
                }
            };
        mainRequestQueue.getRequestQueue().add(employeeUpdateRequest);
    }

    private MutableLiveData<Long> deleted;
    public MutableLiveData<Long> getDeleted(){
        if(deleted==null) {
            deleted = new MutableLiveData<>();
        }
        return deleted;
    }
    public void getDeleted(long id){
        String url = this.url + "/"+id;
        Log.i(TAG,"url = "+url);
        StringRequest deleteByIdRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getDeleted().setValue( (Long)getType(response,Long.class));
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
        mainRequestQueue.getRequestQueue().add(deleteByIdRequest);
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

    private Object getType(String from, Class to){
        Object o = null;
        try {
            o = mapper.readValue(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

    private ArrayList<Employee> getList(String response) {
        ArrayList<Employee> list = null;
        try {
            list = mapper.readValue(response,new TypeReference<ArrayList<Employee>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
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

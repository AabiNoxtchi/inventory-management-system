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
import com.example.inventoryui.Models.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeesData extends AndroidViewModel {
    private MainRequestQueue mainRequestQueue;
    private Long userId;
    private String authToken;

    public EmployeesData(@NonNull Application application) {
        super(application);

        this.mainRequestQueue = MainRequestQueue.getInstance(application);
        this.userId=((AuthenticationManager)this.getApplication()).getLoggedUser().getId();
        this.authToken=((AuthenticationManager)this.getApplication()).getAuthToken();
    }

    private MutableLiveData<ArrayList<Employee>> employees;
    public MutableLiveData<ArrayList<Employee>> getAllEmployeesForUser(){
        if(employees==null)
            employees=new MutableLiveData<>();

        String url ="http://192.168.1.2:8080/employees/"+userId;

        StringRequest employeesForUserRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(getApplication(),response, Toast.LENGTH_LONG).show();

                ObjectMapper om=new ObjectMapper();
                ArrayList<Employee> list = null;
                try {
                    list = om.readValue(response,new TypeReference<ArrayList<Employee>>(){});

                } catch (IOException e) {
                   // Toast.makeText(getApplication(),"exception parsing ", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
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
                HashMap<String, String> map = new HashMap();
                String token="Bearer "+ authToken;
                map.put("Authorization", token);

                return map;
            }
        };
        mainRequestQueue.getRequestQueue().add(employeesForUserRequest);
        return employees;
    }


    private MutableLiveData<Boolean> insertedEmployee;
    public MutableLiveData<Boolean> getInsertedEmployee(){
        if(insertedEmployee==null) {
            insertedEmployee = new MutableLiveData<>();
        }
        return insertedEmployee;
    }

    public void insertEmployee(Employee employee) throws JSONException {

        String url ="http://192.168.1.2:8080/employees/add/"+userId;

        SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");//"dd-MM-yyyy hh:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        JSONObject json = null;
        try {
            json=new JSONObject(mapper.writeValueAsString(employee));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest insertEmployeeRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
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
                HashMap<String, String> map = new HashMap();
                String token="Bearer "+ authToken;
                map.put("Authorization", token);

                return map;
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

    public void updateEmployee(Employee employee) throws JSONException {

        String url ="http://192.168.1.2:8080/employees/"+userId;

        final ObjectMapper mapper = new ObjectMapper();
        JSONObject json = null;
        try {
            json=new JSONObject(mapper.writeValueAsString(employee));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest employeeUpdateRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                json,
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
                HashMap<String, String> map = new HashMap();
                String token="Bearer "+ authToken;
                map.put("Authorization", token);

                return map;
            }
        };
        mainRequestQueue.getRequestQueue().add(employeeUpdateRequest);
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

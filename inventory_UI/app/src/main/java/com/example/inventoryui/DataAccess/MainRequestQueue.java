package com.example.inventoryui.DataAccess;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainRequestQueue {

    public static final String TAG = MainRequestQueue.class.getSimpleName();

    private static MainRequestQueue instance;
    private Context context;
    private RequestQueue requestQueue;
    //192.168.1.12
    //public static final String BASE_URL = "http://192.168.1.2:8080/api/inventory";

    private MainRequestQueue(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();

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



}

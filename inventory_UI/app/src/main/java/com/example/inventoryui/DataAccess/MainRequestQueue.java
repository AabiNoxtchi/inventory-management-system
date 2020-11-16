package com.example.inventoryui.DataAccess;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainRequestQueue {

    private static MainRequestQueue instance;
    private Context context;
    private RequestQueue requestQueue;
    public static final String BASE_URL = "http://192.168.1.3:8080/api/inventory";

    private MainRequestQueue(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized MainRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new MainRequestQueue(context);
        }
        return instance;
    }

}

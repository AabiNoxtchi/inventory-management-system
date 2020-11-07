package com.example.inventoryui.Services;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.inventoryui.Models.AuthenticationManager;

public class AppLifecycleObserver implements LifecycleObserver {

    private static final String TAG = "MyActivity_AppObserver";
    private Context context;

    public AppLifecycleObserver(Context context){
        this.context=context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.i(TAG,"app is in forground");
        ((AuthenticationManager) this.context.getApplicationContext()) .setForground(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.i(TAG,"app is in background");
        ((AuthenticationManager) this.context.getApplicationContext()) .setForground(false);
    }
}

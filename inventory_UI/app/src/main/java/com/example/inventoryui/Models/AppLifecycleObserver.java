package com.example.inventoryui.Models;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class AppLifecycleObserver implements LifecycleObserver {

    private Context context;
    private static final String TAG = AppLifecycleObserver.class.getName();

    public AppLifecycleObserver(Context context){
        this.context=context;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        //run the code we need
        Log.i(TAG,"app is in forground");
        ((AuthenticationManager) this.context.getApplicationContext()) .setForground(true);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        //run the code we need
        Log.i(TAG,"app is in background");
        ((AuthenticationManager) this.context.getApplicationContext()) .setForground(false);
    }
}

package com.example.inventoryui.Models;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.inventoryui.Activities.MainActivity;
import com.example.inventoryui.Services.AppLifecycleObserver;
import com.example.inventoryui.Services.SseListner;

public class AuthenticationManager extends Application {

    private static final String TAG = "MyActivity_Manager";

    private User LoggedUser;
    private String authToken;
    private SseListner sseListner;
    AppLifecycleObserver appLifecycleObserver ;


    private boolean isForground=false;
    private static boolean activityVisible;
    private static Activity activeActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "AuthenticationManager.onCreate");
        appLifecycleObserver = new AppLifecycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);

    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getLoggedUser() {
        return LoggedUser;
    }

    public void setLoggedUser(User loggedUser) {
            LoggedUser = loggedUser;
           // Log.i(TAG,"role = "+ loggedUser.getRole().name());
            if(this.LoggedUser!=null&& this.LoggedUser.getRole().equals(Role.ROLE_Mol))
            {
                sseListner = SseListner.getInstance(this,authToken);
                sseListner.startOksse();
            }
    }

    public boolean isForground() {
        return isForground;
    }

    public void setForground(boolean forground) {
        isForground = forground;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
        Log.i(TAG,"isActivityVisible = "+activityVisible);
    }

    public static void activityPaused() {
        activityVisible = false;
        Log.i(TAG,"isActivityVisible = "+activityVisible);
    }

    public static Activity getActiveActivity(){
        return activeActivity;
    }

    public static void setActiveActivity(Activity activity){
        activeActivity=activity;
    }


    public void logout(){

        setLoggedUser(null);
        setAuthToken(null);
        if(sseListner!=null)
            sseListner.close();
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}

package com.example.inventoryui.Models;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.inventoryui.DataAccess.SseListner;

public class AuthenticationManager extends Application {

    private static final String TAG = "My_Activity"+AuthenticationManager.class.getName();

    private User LoggedUser;
    private String authToken;
    private SseListner sseListner;

    private boolean isForground=false;

    private static boolean activityVisible;

    private  static Activity activeActivity;

    //private static String activeActivity;

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
        //Log.i(TAG,"activeActivity = "+activeActivity.getClass().getName());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppLifecycleObserver appLifecycleObserver = new AppLifecycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
        Log.i(TAG," triggered Application.oncreate() ");
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
            if(this.LoggedUser!=null&& this.LoggedUser.getRole().equals(Role.ROLE_Mol))
            {
                sseListner=new SseListner(this,authToken);
                sseListner.startOksse();
            }
    }

    public boolean isForground() {
        return isForground;
    }

    public void setForground(boolean forground) {
        isForground = forground;
    }

    public void logout(){

        setLoggedUser(null);
        setAuthToken(null);
        if(sseListner!=null)
            sseListner.close();
    }
}

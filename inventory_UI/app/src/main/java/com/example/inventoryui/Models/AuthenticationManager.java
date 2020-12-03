package com.example.inventoryui.Models;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.inventoryui.Activities.MainActivity;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.Services.AppLifecycleObserver;
import com.example.inventoryui.Services.SseListner;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AuthenticationManager extends Application {

    private static final String TAG = "MyActivity_Manager";

    private User LoggedUser;
    private String authToken;
    private SseListner sseListner;
    AppLifecycleObserver appLifecycleObserver ;

    private boolean isForground=false;
    private static boolean activityVisible;
    private static Activity activeActivity;

    final public String BASE_URL = "http://192.168.1.2:8080/api/inventory";
    final public SimpleDateFormat ft= new SimpleDateFormat("E yyyy.MM.dd ", Locale.ENGLISH);

    @Override
    public void onCreate() {
        super.onCreate();
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
            if(this.LoggedUser!=null&& this.LoggedUser.getRole().equals(Role.ROLE_Mol))
            {
                sseListner = SseListner.getInstance(this,authToken,BASE_URL);
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
    }

    public static void activityPaused() {
        activityVisible = false;
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
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}

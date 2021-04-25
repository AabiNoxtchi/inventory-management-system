package com.example.inventoryui.Models;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.inventoryui.Activities.MainActivity;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.Services.AppLifecycleObserver;
import com.example.inventoryui.Services.SseListner;
import com.example.inventoryui.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AuthenticationManager extends Application {

    private static final String TAG = "MyActivity_Manager";

    private User LoggedUser;
    private String authToken;
    private SseListner sseListner;
    AppLifecycleObserver appLifecycleObserver ;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String LoggedUserName = "LoggedUser";
    public static final String authTokenName = "authToken";

    private boolean isForground=false;
    private static boolean activityVisible;
    private static Activity activeActivity;

    final public String BASE_URL = "http://192.168.1.8:8080/api/inventory";
    final public SimpleDateFormat ft= new SimpleDateFormat("E yyyy.MM.dd ", Locale.ENGLISH);

    @Override
    public void onCreate() {
        super.onCreate();

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

       /* SharedPreferences.Editor editor = sharedpreferences.edit();
        // editor.clear();
        editor.remove(LoggedUserName); // will delete key name
        editor.remove(authTokenName);
        editor.commit();*/

        if (sharedpreferences.contains(LoggedUserName)) {
            LoggedUser = (User)Utils.getType(sharedpreferences.getString(LoggedUserName, null),User.class);
        }
        if (sharedpreferences.contains(authTokenName)) {
            authToken = sharedpreferences.getString(authTokenName, null);

        }

        Log.i(TAG,"on create Logged user = "+LoggedUser);
        Log.i(TAG,"on create authtoken = "+authToken);

        appLifecycleObserver = new AppLifecycleObserver(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);

    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;

        SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.putString(LoggedUserName, getLoggedUserString());
        if(authToken==null)editor.remove(authTokenName);
        else editor.putString(authTokenName, authToken);
        editor.commit();
    }

    public User getLoggedUser() {
        return LoggedUser;
    }

    public void setLoggedUser(User loggedUser) {

        LoggedUser = loggedUser;

        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(loggedUser==null) editor.remove(LoggedUserName);
        else editor.putString(LoggedUserName, getLoggedUserString());
        editor.commit();

        if(this.LoggedUser!=null&& ( this.LoggedUser.getRole().equals(Role.ROLE_Mol) ||
                this.LoggedUser.getRole().equals(Role.ROLE_Employee)))
        {
            sseListner = SseListner.getInstance(this,authToken,BASE_URL);
            sseListner.startOksse();
        }
    }

    private String getLoggedUserString() {

        return (Utils.getJsonObject(LoggedUser)).toString();
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

       // Log.i(TAG,"before sharedpreferences = "+sharedpreferences.getAll());
        //SharedPreferences.Editor editor = sharedpreferences.edit();
       // editor.clear();
       // will delete key name


       // Log.i(TAG,"after sharedpreferences = "+sharedpreferences.getAll());
        //editor.commit();

        if(sseListner!=null)
            sseListner.close();

        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}

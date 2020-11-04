package com.example.inventoryui.Models;

import android.app.Application;

import com.example.inventoryui.DataAccess.SseListner;

public class AuthenticationManager extends Application {

    private User LoggedUser;
    private String authToken;
    SseListner sseListner;

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


    public void logout(){

        setLoggedUser(null);
        setAuthToken(null);

        if(sseListner!=null)
            sseListner.close();
    }
}

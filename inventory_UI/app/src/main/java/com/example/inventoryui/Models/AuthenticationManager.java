package com.example.inventoryui.Models;

import android.app.Application;

public class AuthenticationManager extends Application {

    private User LoggedUser;
    private String authToken;



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
    }



    public void logout(){

        setLoggedUser(null);
        setAuthToken(null);

    }


}

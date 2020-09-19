package com.example.inventoryui.Models;

import android.app.Application;

public class AuthenticationManager extends Application {

    private User LoggedUser;

    public User getLoggedUser() {
        return LoggedUser;
    }

    public void setLoggedUser(User loggedUser) {

            LoggedUser = loggedUser;
    }

    public void logout(){

        setLoggedUser(null);

    }


}

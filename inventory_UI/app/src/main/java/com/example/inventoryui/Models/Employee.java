package com.example.inventoryui.Models;

public class Employee extends AbstractUser {


    private User user;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }


}

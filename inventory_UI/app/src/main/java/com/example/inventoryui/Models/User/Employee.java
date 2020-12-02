package com.example.inventoryui.Models.User;

public class Employee extends AbstractUser {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.example.inventoryui.Models.User;

import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Shared.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseModel implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private Role role;
    private List<Product> products;

    public User() {}

    public User(Long id,String userName,String role){
        setId(id);
        setUserName(userName);
        setRole(Role.valueOf(role));
    }

    public User(String userName, String password,
                String email, Role role) {
       // super();
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Product> getProducts() {
        if (products==null)
            products=new ArrayList<>();
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}

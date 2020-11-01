package com.example.inventoryui.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Controllers.Admin.AdminMainActivity;
import com.example.inventoryui.Controllers.Products.ProductsMainActivity;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LoginRequest;
import com.example.inventoryui.Models.LoginResponse;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;

public class MainActivity extends AppCompatActivity {

     TextView userNameTextView;
     TextView pswrdTextView;
     Button loginButton;
     private UsersData usersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameTextView=findViewById(R.id.userNameLoginTextView);
        pswrdTextView=findViewById(R.id.pswrdTextView);
        loginButton=findViewById(R.id.loginButton);

        usersData= new ViewModelProvider(this).get(UsersData.class);

        loginButtonOnClickAction();
        loginObserve();

    }

    private void loginObserve() {

        usersData.getLoggedUser().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {

                if (loginResponse != null) {

                    final AuthenticationManager auth = (AuthenticationManager) getApplicationContext();
                    User user=new User(loginResponse.getId(),
                              loginResponse.getUserName(),
                              loginResponse.getRole());
                    auth.setLoggedUser(user);
                    auth.setAuthToken(loginResponse.getToken());

                    Toast.makeText(getApplication(), " Welcome " +auth.getLoggedUser().getUserName(),
                            Toast.LENGTH_LONG).show();

                    if(auth.getLoggedUser().getRole().equals(Role.ROLE_Admin)){
                        //send to admin activity
                        Intent i = new Intent(MainActivity.this, AdminMainActivity.class);
                        startActivity(i);

                    }else if(auth.getLoggedUser().getRole().equals(Role.ROLE_Mol) ||
                            auth.getLoggedUser().getRole().equals(Role.ROLE_Employee)){
                        //send to mol activity
                        Intent i = new Intent(MainActivity.this, ProductsMainActivity.class);
                        startActivity(i);
                    }

                }else{
                    Toast.makeText(MainActivity.this, " user name or password error !!! " ,
                            Toast.LENGTH_LONG).show();
                }

                //Complete and destroy login activity once successful
                //MainActivity.this.finish();
            }


        });
    }

    private void loginButtonOnClickAction(){

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginRequest loginRequest=new LoginRequest(
                                  userNameTextView.getText().toString(),pswrdTextView.getText().toString());
                        usersData.getLoggedUser(loginRequest);
                    }
                }
        );
    }
}

package com.example.inventoryui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Activities.Admin.AdminMainActivity;
import com.example.inventoryui.Activities.Products.ProductsMainActivity;
import com.example.inventoryui.DataAccess.LoginData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LoginRequest;
import com.example.inventoryui.Models.LoginResponse;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;


public class MainActivity extends AppCompatActivity {

     final String TAG="MyActivity_Main";
     private TextView userNameTextView;
     private TextView pswrdTextView;
     private Button loginButton;
    // private UsersData usersData;
     private LoginData loginData;
     AuthenticationManager auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // auth =(AuthenticationManager) getApplicationContext();
        auth = ((AuthenticationManager)this.getApplication());
        if(auth.getLoggedUser()!=null)
            sendToActivity(auth.getLoggedUser());
        else {
            userNameTextView = findViewById(R.id.userNameLoginTextView);
            pswrdTextView = findViewById(R.id.pswrdTextView);
            loginButton = findViewById(R.id.loginButton);

           // usersData = new ViewModelProvider(this).get(UsersData.class);
            loginData = new ViewModelProvider(this).get(LoginData.class);
            loginButtonOnClickAction();
            loginObserve();
        }
    }

    private void loginObserve() {

        loginData.getLoggedUser().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null) {
                    User user=new User(loginResponse.getId(),
                              loginResponse.getUserName(),
                              loginResponse.getRole());
                    auth.setAuthToken(loginResponse.getToken());
                    auth.setLoggedUser(user);

                    Toast.makeText(getApplication(), " Welcome " +auth.getLoggedUser().getUserName(),
                            Toast.LENGTH_LONG).show();
                    if(auth.getLoggedUser()!=null)
                          sendToActivity(auth.getLoggedUser());
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, " user name or password error !!! " ,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendToActivity(User loggedUser) {
        if(loggedUser.getRole().equals(Role.ROLE_Admin)){
            Intent i = new Intent(MainActivity.this, AdminMainActivity.class);
            startActivity(i);
        }else if(loggedUser.getRole().equals(Role.ROLE_Mol) ||
               loggedUser.getRole().equals(Role.ROLE_Employee)){
                Intent i = new Intent(MainActivity.this, ProductsMainActivity.class);
               // Intent i = new Intent(MainActivity.this, Test2Activity.class);
                startActivity(i);
        }
    }

    private void loginButtonOnClickAction(){
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginRequest loginRequest=new LoginRequest(
                                  userNameTextView.getText().toString(),pswrdTextView.getText().toString());
                        loginData.getLoggedUser(loginRequest);
                    }
                }
        );
    }
}

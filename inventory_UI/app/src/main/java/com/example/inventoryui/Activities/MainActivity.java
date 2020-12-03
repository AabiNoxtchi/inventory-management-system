package com.example.inventoryui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Activities.User.UsersMainActivity;
import com.example.inventoryui.Activities.Products.ProductsMainActivity;
import com.example.inventoryui.DataAccess.LoginData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LogInRegister.LoginRequest;
import com.example.inventoryui.Models.LogInRegister.LoginResponse;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {

     final String TAG="MyActivity_Main";
     private TextView userNameTextView;
     private TextView pswrdTextView;
     private Button loginButton;

     private TextInputLayout userName_logIn_layout;
     private TextInputLayout password_logIn_layout;
    // private UsersData usersData;
     private LoginData loginData;
     AuthenticationManager auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // auth =(AuthenticationManager) getApplicationContext();
        auth = ((AuthenticationManager)this.getApplication());
        if(auth.getLoggedUser()!=null)
            sendToActivity(auth.getLoggedUser());
        else {
            userNameTextView = findViewById(R.id.userNameLoginTextView);
            pswrdTextView = findViewById(R.id.pswrdTextView);
            loginButton = findViewById(R.id.loginButton);

            userName_logIn_layout = findViewById(R.id.userName_logIn_layout);
            password_logIn_layout = findViewById(R.id.password_logIn_layout);
            OnTextInputClick(userName_logIn_layout,userNameTextView);
            OnTextInputClick(password_logIn_layout,pswrdTextView);

           // usersData = new ViewModelProvider(this).get(UsersData.class);
            loginData = new ViewModelProvider(this).get(LoginData.class);
            loginButtonOnClickAction();
            loginObserve();

        }
    }

    private void OnTextInputClick(TextInputLayout txtLayout, TextView txtView) {

        txtView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if(txtLayout.isErrorEnabled())txtLayout.setError(null);

            }
        });

    }

    private void loginObserve() {

        loginData.getLoggedUser().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null) {
                    User user = new User(loginResponse.getId(),
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
            Intent i = new Intent(MainActivity.this, UsersMainActivity.class);
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
                        boolean valid = true;
                        if(TextUtils.isEmpty(userNameTextView.getText().toString())) {
                            userName_logIn_layout.setError("this field is required !!!");
                            valid = false;
                        }
                        if(TextUtils.isEmpty(pswrdTextView.getText().toString())) {
                            password_logIn_layout.setError("this field is required !!!");
                            valid = false;
                        }

                        if(valid) {
                            LoginRequest loginRequest = new LoginRequest(
                                    userNameTextView.getText().toString(), pswrdTextView.getText().toString());
                            loginData.getLoggedUser(loginRequest);
                        }
                    }
                }
        );
    }
}

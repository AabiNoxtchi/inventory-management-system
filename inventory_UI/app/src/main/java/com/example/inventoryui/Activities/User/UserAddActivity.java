package com.example.inventoryui.Activities.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Activities.Products.ProductToUser2;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LogInRegister.RegisterRequest;
import com.example.inventoryui.Models.LogInRegister.RegisterResponse;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class UserAddActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity_UserAdd";
    TextView firstNameTextView;
    TextView lastNameTextView;
    TextView titleTextView;
    TextView userNameTextView;
    TextView emailTextView;
    TextView passwordTextView;
    TextView confirmPasswordTextView;
    TextInputLayout passwordEditText_TextInputLayout;
    TextInputLayout confirmPasswordEditText_TextInputLayout;
    Button saveButton;
    Button cancelButton;
    Button changePasswordButton;

    UsersData usersData;
    User userForUpdate;
    User loggedUser;


    String firstName;//=firstNameTextView.getText().toString();
    String lastName;//=lastNameTextView.getText().toString();
    String userName;//=userNameTextView.getText().toString();
    String email;//=emailTextView.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_mol);

        Toolbar toolbar = findViewById(R.id.toolbar_addProduct);
        setSupportActionBar(toolbar);

        loggedUser=((AuthenticationManager)this.getApplication()).getLoggedUser();

        firstNameTextView=findViewById(R.id.firstNameEditText);
        lastNameTextView=findViewById(R.id.lastNameEditText);
        titleTextView=findViewById(R.id.titleTextView);
        userNameTextView=findViewById(R.id.userNameEditText);
        emailTextView=findViewById(R.id.emailEditText);
        passwordTextView=findViewById(R.id.passwordEditText);
        confirmPasswordTextView=findViewById(R.id.confirmPasswordEditText);
        passwordEditText_TextInputLayout=findViewById(R.id.passwordEditText_TextInputLayout);
        confirmPasswordEditText_TextInputLayout=findViewById(R.id.confirmPasswordEditText_TextInputLayout);
        saveButton=findViewById(R.id.saveButton);
        cancelButton=findViewById(R.id.cancelButton);
        changePasswordButton=findViewById(R.id.changePassword);

        usersData= new ViewModelProvider(this).get(UsersData.class);

        Intent i=getIntent();
        if(i.hasExtra("userForUpdate")) {
           userForUpdate = (User) i.getSerializableExtra("userForUpdate");
            initializeFields();
        }else if(i.hasExtra("userIdForUpdate")) {//employeeIdForUpdate
            Long userId = (long) i.getLongExtra("userIdForUpdate", 0);
            usersData.getById(userId);
            usersData.getItem().observe(UserAddActivity.this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    userForUpdate = user;
                    initializeFields();
                }
            });
        }

        saveButtonOnClick();
        cancelButtonOnClick();
        insertUserObserve();
        deleteUserObserver();
    }

    private void deleteUserObserver(){
       usersData.getDeletedId().observe(UserAddActivity.this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(Objects.equals(aLong,userForUpdate.getId())) {

                    Toast.makeText(UserAddActivity.this, "User has been deleted !!!", Toast.LENGTH_LONG);

                    redirectToActivity();
                }else{
                    Toast.makeText(UserAddActivity.this, "User could't be deleted !!!", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void insertUserObserve() {
        usersData.getInsertedUser().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(RegisterResponse registerResponse) {
               if(registerResponse!=null){
                   Toast.makeText(getApplication(),registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                   Log.i(TAG,registerResponse.getMessage());
                    if(registerResponse.isRefreshToken()&&registerResponse.getJwtToken().length()>0) {
                          Log.i(TAG,"refreshing token");
                        ((AuthenticationManager) UserAddActivity.this.getApplication()).setAuthToken(registerResponse.getJwtToken());
                    }
                   if(loggedUser.getRole().equals(Role.ROLE_Mol)) {
                       updateUserForUpdate();
                   }
                   redirectToActivity();
                }else{
                    Toast.makeText(getApplication(),"error couldn't save user !!! ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUserForUpdate() {
        userForUpdate.setEmail(email);
        userForUpdate.setUserName(userName);
        userForUpdate.setFirstName(firstName);
        userForUpdate.setLastName(lastName);
    }

    private void initializeFields() {
        if(userForUpdate==null) {
            titleTextView.setText("Add New User");
        }
        else {
            titleTextView.setText("Edit User");
            firstNameTextView.setText(userForUpdate.getFirstName());
            lastNameTextView.setText(userForUpdate.getLastName());
            userNameTextView.setText(userForUpdate.getUserName());
            emailTextView.setText(userForUpdate.getEmail());

            passwordEditText_TextInputLayout.setVisibility(View.GONE);
            confirmPasswordEditText_TextInputLayout.setVisibility(View.GONE);
            changePasswordButton.setVisibility(View.VISIBLE);
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(changePasswordButton.getText().toString().equals("Change Password"))
                    {
                        passwordEditText_TextInputLayout.setVisibility(View.VISIBLE);
                        confirmPasswordEditText_TextInputLayout.setVisibility(View.VISIBLE);
                        changePasswordButton.setText("Leave Password");
                    }else{
                        passwordEditText_TextInputLayout.setVisibility(View.GONE);
                        confirmPasswordEditText_TextInputLayout.setVisibility(View.GONE);
                        changePasswordButton.setText("Change Password");
                    }
                }
            });
        }
    }

    private void cancelButtonOnClick() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToActivity();
            }
        });
    }

    private void redirectToActivity(){
        if(loggedUser.getRole().equals(Role.ROLE_Admin)) {
            Intent i = new Intent(UserAddActivity.this, UsersMainActivity.class);
            startActivity(i);
        }else{
            if(userForUpdate!=null) {
                Intent i = new Intent(UserAddActivity.this, ProductToUser2.class);
                i.putExtra("userForUpdate", userForUpdate);
                startActivity(i);
            }else {
                Intent i = new Intent(UserAddActivity.this, UsersMainActivity.class);
                startActivity(i);
            }
        }
    }

    private void saveButtonOnClick() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               saveUser();
            }
        });
    }

    private void saveUser(){
        /*String*/ firstName=firstNameTextView.getText().toString();
        /*String*/ lastName=lastNameTextView.getText().toString();
        /*String*/ userName=userNameTextView.getText().toString();
        /*String*/ email=emailTextView.getText().toString();
        String password=passwordTextView.getText().toString();
        String confirmPassword=confirmPasswordTextView.getText().toString();
        boolean valid=validateInputs(userName,email,password,confirmPassword);
        if(valid)
        {
            RegisterRequest registerRequest=new RegisterRequest(
                    firstName,lastName,userName,email,password);
            if(userForUpdate!=null) {
                registerRequest.setId(userForUpdate.getId());

            }
            usersData.insertUser(registerRequest);
        }
    }

    private boolean validateInputs(String userName, String email, String password, String confirmPassword) {

        boolean valid=true;

        if(userName.length()<3){
            userNameTextView.setError("too short user name !!!");
            valid=false;
        }
        if(TextUtils.isEmpty(email) ){
            emailTextView.setError("required field !!!");
            valid=false;
        }
        if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTextView.setError("required valid email address !!!");
            valid=false;
        }
        if(userForUpdate==null||
                (userForUpdate!=null&&!changePasswordButton.getText().toString().equals("Change Password"))){
        if(password.length()<6){
            passwordTextView.setError("password has to be at least 6 characters long !!!");
            valid=false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordTextView.setError("confirm password and password dont match !!!");
            valid=false;
        }}
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.user_add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.icon_delete_user:
                if (userForUpdate==null )  redirectToActivity();

                new AlertDialog.Builder(UserAddActivity.this)
                        .setTitle("Delete User").setMessage(" sure you want to delete "
                        + userForUpdate.getUserName()+" ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(loggedUser.getRole().equals(Role.ROLE_Admin)){
                                    usersData.deleteId(userForUpdate.getId());
                                }
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
                return true;
            case R.id.icon_edit_user:
                saveUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(loggedUser.getRole().equals(Role.ROLE_Mol)) {
            AuthenticationManager.activityResumed();
            AuthenticationManager.setActiveActivity(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(loggedUser.getRole().equals(Role.ROLE_Mol)) {
            AuthenticationManager.activityPaused();
            AuthenticationManager.setActiveActivity(null);
        }
    }
}

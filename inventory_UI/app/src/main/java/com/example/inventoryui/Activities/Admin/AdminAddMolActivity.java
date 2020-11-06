package com.example.inventoryui.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Activities.Employees.EmployeesMainActivity;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.RegisterRequest;
import com.example.inventoryui.Models.RegisterResponse;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;

public class AdminAddMolActivity extends AppCompatActivity {


    TextView firstNameTextView;
    TextView lastNameTextView;
    TextView titleTextView;
    TextView userNameTextView;
    TextView emailTextView;
    TextView passwordTextView;
    TextView confirmPasswordTextView;
    Button saveButton;
    Button cancelButton;
    Button changePasswordButton;

    UsersData usersData;
    User userForUpdate;

    private static final String TAG = "MyActivity_AddUser";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_mol);

        //Toast.makeText(AdminAddMolActivity.this,"in add mol",Toast.LENGTH_LONG).show();

        firstNameTextView=findViewById(R.id.firstNameEditText);
        lastNameTextView=findViewById(R.id.lastNameEditText);
        titleTextView=findViewById(R.id.titleTextView);
        userNameTextView=findViewById(R.id.userNameEditText);
        emailTextView=findViewById(R.id.emailEditText);
        passwordTextView=findViewById(R.id.passwordEditText);
        confirmPasswordTextView=findViewById(R.id.confirmPasswordEditText);
        saveButton=findViewById(R.id.saveButton);
        cancelButton=findViewById(R.id.cancelButton);
        changePasswordButton=findViewById(R.id.changePassword);

        usersData= new ViewModelProvider(this).get(UsersData.class);

        Intent i=getIntent();
        if(i.hasExtra("userForUpdate")) {
           userForUpdate = (User) i.getSerializableExtra("userForUpdate");

        }


        initializeFields();

        saveButtonOnClick();

        cancelButtonOnClick();

        insertUserObserve();


    }



    private void insertUserObserve() {

        /* usersData.getInsertedUser().observe(this, new Observer<RegisterResponse>(){

             @Override
             public void onChanged(RegisterResponse registerResponse) {
                // Toast.makeText(getApplication(),aBoolean.toString(), Toast.LENGTH_LONG).show();
                 // refresh jwt token //

                 Toast.makeText(getApplication(), registerResponse.getMessage()+registerResponse.isRefreshToken(), Toast.LENGTH_LONG).show();
                *//* if(registerResponse.isRefreshToken()&&registerResponse.getJwtToken().length()>0){
                     ((AuthenticationManager)AdminAddMolActivity.this.getApplication()).setAuthToken(registerResponse.getJwtToken());
                     //Toast.makeText(getApplication(),registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                     Intent i = new Intent(AdminAddMolActivity.this, AdminMainActivity.class);
                     startActivity(i);
                 }else{
                     Toast.makeText(getApplication(),"error couldn't save user !!! ",Toast.LENGTH_LONG).show();
                 }*//*
             }
         });*/

        usersData.getInsertedUser().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(RegisterResponse registerResponse) {

                Toast.makeText(AdminAddMolActivity.this,registerResponse.getMessage(),Toast.LENGTH_LONG).show();

               if(registerResponse!=null){
                    if(registerResponse.isRefreshToken()&&registerResponse.getJwtToken().length()>0) {
                        ((AuthenticationManager) AdminAddMolActivity.this.getApplication()).setAuthToken(registerResponse.getJwtToken());
                    }
                    Toast.makeText(getApplication(),registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                    /////////important////////////////
                   if(((AuthenticationManager) getApplicationContext()).getLoggedUser().getRole().equals(Role.ROLE_Admin)) {
                       Intent i = new Intent(AdminAddMolActivity.this, AdminMainActivity.class);
                       startActivity(i);
                   }else{
                       Intent i = new Intent(AdminAddMolActivity.this, EmployeesMainActivity.class);
                       startActivity(i);
                   }
                }else{
                    Toast.makeText(getApplication(),"error couldn't save user !!! ",Toast.LENGTH_LONG).show();
                }
            }
        });
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

            passwordTextView.setVisibility(View.GONE);
            confirmPasswordTextView.setVisibility(View.GONE);
            changePasswordButton.setVisibility(View.VISIBLE);
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(changePasswordButton.getText().toString()=="Change Password")
                    {
                        passwordTextView.setVisibility(View.VISIBLE);
                        confirmPasswordTextView.setVisibility(View.VISIBLE);
                        changePasswordButton.setText("Leave Password");
                    }else{
                        passwordTextView.setVisibility(View.GONE);
                        confirmPasswordTextView.setVisibility(View.GONE);
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
                Intent i = new Intent(AdminAddMolActivity.this, AdminMainActivity.class);
                startActivity(i);
            }
        });

    }

    private void saveButtonOnClick() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName=firstNameTextView.getText().toString();
                String lastName=lastNameTextView.getText().toString();
                String userName=userNameTextView.getText().toString();
                String email=emailTextView.getText().toString();
                String password=passwordTextView.getText().toString();
                String confirmPassword=confirmPasswordTextView.getText().toString();
                boolean valid=validateInputs(userName,email,password,confirmPassword);

                if(valid)
                {
                    RegisterRequest registerRequest=new RegisterRequest(
                            firstName,lastName,userName,email,password);
                    if(userForUpdate!=null) {
                        registerRequest.setId(userForUpdate.getId());
                       /* if(confirmCurrentUserCredentialsChange(registerRequest)){

                            //show dialog
                        }*/
                    }

                   // Toast.makeText(getApplication()," valid input ", Toast.LENGTH_LONG).show();


                    usersData.insertUser(registerRequest);
                }
            }
        });
    }

    /*private boolean confirmCurrentUserCredentialsChange(RegisterRequest registerRequest){

        if(registerRequest.getId()==((AuthenticationManager)this.getApplication()).getLoggedUser().getId()&&
                (
                registerRequest.getUsername()!=((AuthenticationManager)this.getApplication()).getLoggedUser().getUserName()||
                        (
                            registerRequest.getPassword()!=null&&
                            registerRequest.getPassword().length()>6
                        )
                )
        )
             return true;

        return false;
    }*/

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
            Log.i(TAG,"email not valid");
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

        //Toast.makeText(getApplication()," changePasswordButton.getText().toString() = "+
              //  changePasswordButton.getText().toString() +"  ,userfor update  .null = "+userForUpdate, Toast.LENGTH_LONG).show();

        return valid;
    }
}

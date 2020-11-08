package com.example.inventoryui.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.Activities.Employees.EmployeeAddActivity;
import com.example.inventoryui.Activities.Employees.EmployeesMainActivity;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AbstractUser;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.RegisterRequest;
import com.example.inventoryui.Models.RegisterResponse;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;

public class AdminAddMolActivity extends AppCompatActivity {

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
    //User userForUpdate;
    AbstractUser userForUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_mol);

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
           userForUpdate = (AbstractUser) i.getSerializableExtra("userForUpdate");
        }
        initializeFields();
        saveButtonOnClick();
        cancelButtonOnClick();
        insertUserObserve();
    }

    private void insertUserObserve() {
        usersData.getInsertedUser().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(RegisterResponse registerResponse) {
               if(registerResponse!=null){
                   Toast.makeText(getApplication(),registerResponse.getMessage(), Toast.LENGTH_LONG).show();
                    if(registerResponse.isRefreshToken()&&registerResponse.getJwtToken().length()>0) {
                        ((AuthenticationManager) AdminAddMolActivity.this.getApplication()).setAuthToken(registerResponse.getJwtToken());
                    }
                   redirectToActivity();
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
        if(((AuthenticationManager) getApplicationContext()).getLoggedUser().getRole().equals(Role.ROLE_Admin)) {
            Intent i = new Intent(AdminAddMolActivity.this, AdminMainActivity.class);
            startActivity(i);
        }else{
            if(userForUpdate!=null) {
                Intent i = new Intent(AdminAddMolActivity.this, EmployeeAddActivity.class);
                i.putExtra("employeeIdForUpdate", userForUpdate.getId());
                startActivity(i);
            }else {
                Intent i = new Intent(AdminAddMolActivity.this, EmployeesMainActivity.class);
                startActivity(i);
            }
        }
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
                    }
                    usersData.insertUser(registerRequest);
                }
            }
        });
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
}

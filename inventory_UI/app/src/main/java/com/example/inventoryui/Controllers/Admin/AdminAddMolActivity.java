package com.example.inventoryui.Controllers.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;

public class AdminAddMolActivity extends AppCompatActivity {


    TextView titleTextView;
    TextView userNameTextView;
    TextView passwordTextView;
    TextView confirmPasswordTextView;
    Button saveButton;

    UsersData usersData;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_mol);

        titleTextView=findViewById(R.id.titleTextView);
        userNameTextView=findViewById(R.id.userNameEditText);
        passwordTextView=findViewById(R.id.passwordEditText);
        confirmPasswordTextView=findViewById(R.id.confirmPasswordEditText);
        saveButton=findViewById(R.id.saveButton);

        usersData= new ViewModelProvider(this).get(UsersData.class);
        user=null;

        initializeFields(user);

        saveButtonOnClick();

        insertUserObserve();


    }

    private void insertUserObserve() {

         usersData.getInsertedUser().observe(this, new Observer<Boolean>(){

             @Override
             public void onChanged(Boolean aBoolean) {
                 if(aBoolean){
                    // Toast.makeText(getApplication()," 1 user have been saved successfully ", Toast.LENGTH_LONG).show();
                     Intent i = new Intent(AdminAddMolActivity.this, AdminMainActivity.class);
                     startActivity(i);
                 }else{
                     Toast.makeText(getApplication(),"error couldn't save user !!! ",Toast.LENGTH_LONG).show();
                 }
             }
         });
    }


    private void initializeFields(User user) {

        if(user==null) {
            titleTextView.setText("Add New User");
        }
        else {
            titleTextView.setText("Edit User");
        }
    }

    private void saveButtonOnClick() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=userNameTextView.getText().toString();
                String password=passwordTextView.getText().toString();
                String confirmPassword=confirmPasswordTextView.getText().toString();
                boolean valid=validateInputs(userName,password,confirmPassword);

                if(valid)
                {
                    User user=new User(userName,password, Role.Mol);
                    usersData.insertUser(user);
                }
            }
        });
    }

    private boolean validateInputs(String userName, String password, String confirmPassword) {

        boolean valid=true;

        if(userName.length()<3){
            userNameTextView.setError("too short user name !!!");
            valid=false;
        }
        if(password.length()<3){
            passwordTextView.setError("password too short !!!");
            valid=false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordTextView.setError("confirm password and password dont match !!!");
            valid=false;
        }

        return valid;
    }
}

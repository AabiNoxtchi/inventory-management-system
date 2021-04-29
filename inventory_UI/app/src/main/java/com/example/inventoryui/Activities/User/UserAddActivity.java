package com.example.inventoryui.Activities.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.LogInRegister.RegisterRequest;
import com.example.inventoryui.Models.LogInRegister.RegisterResponse;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class UserAddActivity extends AppCompatActivity {

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

    SearchableSpinner citiesSpinner;
    SearchableSpinner countriesSpinner;
    LinearLayout cityLayout;
    List<SelectItem> cities;
    List<SelectItem> filteredCities;

    UsersData usersData;
    User userForUpdate;
    User loggedUser;

    String firstName;
    String lastName;
    String userName;
    String email;

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

        citiesSpinner = findViewById(R.id.citiesSpinner);

        Intent i = getIntent();

        if(i.hasExtra("userForUpdate")) {

           userForUpdate = (User) i.getSerializableExtra("userForUpdate");
            initializeFields();

        }else if(i.hasExtra("userIdForUpdate")) {

            Long userId = (long) i.getLongExtra("userIdForUpdate", -1);
            if(!(loggedUser.getRole().equals(Role.ROLE_Mol) && userId.equals(-1))) {
                usersData.getById(userId);
                usersData.getItem().observe(UserAddActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        userForUpdate = user;
                        initializeFields();
                    }
                });
            }else
                initializeFields();
        }

        saveButtonOnClick();
        cancelButtonOnClick();
        insertUserObserve();
        deleteUserObserver();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.icon_delete_user:
                if (userForUpdate == null ) redirectToActivity();

                new AlertDialog.Builder(UserAddActivity.this)
                        .setTitle("Delete User").setMessage(" sure you want to delete "
                        + userForUpdate.getUserName()+" ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                usersData.deleteId(userForUpdate.getId());

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

    private void deleteUserObserver(){
       usersData.getDeletedId().observe(UserAddActivity.this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(Objects.equals(aLong, userForUpdate.getId())) {

                    Toast.makeText(UserAddActivity.this,
                            "User has been deleted !!!", Toast.LENGTH_LONG);

                    redirectToActivity();
                }else{
                    Toast.makeText(UserAddActivity.this,
                            "User could't be deleted !!!", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void insertUserObserve() {
        usersData.getInsertedUser().observe(this, new Observer<RegisterResponse>() {
            @Override
            public void onChanged(RegisterResponse registerResponse) {
               if(registerResponse!=null){

                   Toast.makeText(getApplication(),
                           registerResponse.getMessage(), Toast.LENGTH_LONG).show();

                   if(registerResponse.isRefreshToken()&&registerResponse.getJwtToken().length()>0) {
                           ((AuthenticationManager) UserAddActivity.this.getApplication())
                                   .setAuthToken(registerResponse.getJwtToken());
                    }
                   if(loggedUser.getRole().equals(Role.ROLE_Mol)) {
                       updateUserForUpdate();
                   }
                   redirectToActivity();

                }else{
                    Toast.makeText(getApplication(),
                            "error couldn't save user !!! ", Toast.LENGTH_LONG).show();
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
        if(userForUpdate == null) {
            titleTextView.setText("Add New User");
        }
        if(userForUpdate.getCities() != null){
            fillSpinners();
        }
        if(userForUpdate != null && userForUpdate.getId() != null &&  userForUpdate.getId() > 0) {
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

    private void fillSpinners() {
        cityLayout = findViewById(R.id.cityLayout);
        cityLayout.setVisibility(View.VISIBLE);

        countriesSpinner = findViewById(R.id.countriesSpinner);
        List<SelectItem> countries = userForUpdate.getCountries();

        ArrayAdapter adapterCountry = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item);//, countries);

        countries.add(0,new SelectItem("",""));
        adapterCountry.addAll(countries);
        countriesSpinner.setAdapter(adapterCountry);
        countriesSpinner.setTitle("select country");

        if (userForUpdate.getCountryId() != null) {
            int index = getIndex(countries, ""+ userForUpdate.getCountryId());
            countriesSpinner.setSelection(index);
            adapterCountry.notifyDataSetChanged();
       }

        cities = userForUpdate.getCities();
        filteredCities = new ArrayList<>(); // ust once new

        if (userForUpdate.getCountryId() == null)
             filterCities(filteredCities, cities, ""+userForUpdate.getCountryId());
        ArrayAdapter adapterCity = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, filteredCities);



        citiesSpinner.setAdapter(adapterCity);
        citiesSpinner.setTitle("select city");

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String countryValue = countries.get(position).getValue();
                filterCities(filteredCities, cities, countryValue);
                adapterCity.notifyDataSetChanged();

                if(userForUpdate.getCountryId() != null &&
                        (""+userForUpdate.getCountryId()).equals(countryValue)){
                    getUserCity(""+userForUpdate.getCityId(), adapterCity);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getUserCity(String value, ArrayAdapter adapterCity) {
            int index = getIndex(filteredCities, value);
            citiesSpinner.setSelection(index);
            adapterCity.notifyDataSetChanged();
    }

    private List<SelectItem> filterCities(List<SelectItem> list,
                                          List<SelectItem> cities, String countryId) {

        list.clear();
        if(countryId == null)
            for(SelectItem item : cities){
                    list.add(item);
            }
        else
            for(SelectItem item : cities){
                if(item.getFilterBy().equals(countryId))
                    list.add(item);
            }

        list.add(0, new SelectItem("","",""));
        return list;
    }

    private int getIndex(List<SelectItem> list, String value) {

        if(TextUtils.isEmpty(value))return -1;
        int index = IntStream.rangeClosed(0, list.size()-1)
                .filter(i -> //{
                   list.get(i).getValue().equals(value)).findFirst().orElse(-1);
        return index;
    }

    private void cancelButtonOnClick() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void redirectToActivity(){
        Intent i = new Intent(UserAddActivity.this, UsersMainActivity.class);
        startActivity(i);
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
        firstName=firstNameTextView.getText().toString();
        lastName=lastNameTextView.getText().toString();
        userName=userNameTextView.getText().toString();
        email=emailTextView.getText().toString();
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
                (userForUpdate!=null&&!changePasswordButton.getText()
                        .toString().equals("Change Password"))){
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

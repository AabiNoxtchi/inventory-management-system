package com.example.inventoryui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.inventoryui.Activities.Inventory.InventoriesMainActivity;
import com.example.inventoryui.Activities.User.UsersMainActivity;
import com.example.inventoryui.Activities.UserProfile.UserProfilesMainActivity;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;

public class HomeMainActivityMol extends AppCompatActivity {

    AuthenticationManager auth ;
    Button btnToUsers;
    Button btnToProfiles;
    Button btnToInventories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main_mol);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = ((AuthenticationManager)this.getApplication());
        User loggedUser = auth.getLoggedUser();
        if( loggedUser == null || !loggedUser.getRole().equals(Role.ROLE_Mol)){
            Intent i = new Intent(HomeMainActivityMol.this, MainActivity.class);
            startActivity(i);
        }

        btnToUsers = findViewById(R.id.btnToUsers);
        btnToUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUsers();
            }
        });

        btnToProfiles = findViewById(R.id.btnToProfiles);
        btnToProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProfiles();
            }
        });

        btnToInventories=findViewById(R.id.btnToInventories);
        btnToInventories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInventories();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mol_menu,menu);
        menu.findItem(R.id.filter_icon).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                ((AuthenticationManager) this.getApplication()).logout();
                return true;
            case R.id.users:
                toUsers();
                return true;

            case R.id.inventories:
                toInventories();
                return true;

            case R.id.profiles:
                toProfiles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toProfiles() {
        Intent i = new Intent(HomeMainActivityMol.this, UserProfilesMainActivity.class);
        startActivity(i);
    }

    private void toUsers() {
        Intent i = new Intent(HomeMainActivityMol.this, UsersMainActivity.class);
        startActivity(i);
    }

    private void toInventories(){

        Intent v = new Intent(this, InventoriesMainActivity.class);
        startActivity(v);
    }
}

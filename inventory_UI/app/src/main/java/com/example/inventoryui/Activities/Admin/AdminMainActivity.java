package com.example.inventoryui.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {

    FloatingActionButton addFab;
    RecyclerView usersRecyclerView ;
    UsersAdapter usersAdapter;
    private UsersData usersData;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        addFab=findViewById(R.id.addFab);
        addFab.show();
        addFabOnClick();

        usersData= new ViewModelProvider(this).get(UsersData.class);

        usersRecyclerView=findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        users=new ArrayList<>();
        usersAdapter=new UsersAdapter(AdminMainActivity.this, users, new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User item) {
                Intent i = new Intent(AdminMainActivity.this, AdminAddMolActivity.class);
                i.putExtra("userForUpdate", item);
                startActivity(i);
            }
        },new UsersAdapter.OnLongClickListener(){
            @Override
            public void onLongItemClick(User item) {

            }
        });
        usersRecyclerView.setAdapter(usersAdapter);
        getUsers();
    }

    private void addFabOnClick() {
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminMainActivity.this, AdminAddMolActivity.class);
                startActivity(i);
            }
        });
    }

    private void getUsers() {
        usersData.getAllUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> newUsers) {
                users.clear();
                users.addAll(newUsers);
                usersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                ((AuthenticationManager)this.getApplication()).logout();
               /* Intent i=new Intent(AdminMainActivity.this, MainActivity.class);
                startActivity(i);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

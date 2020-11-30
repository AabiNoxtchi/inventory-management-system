package com.example.inventoryui.Activities.Employees;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Admin.AdminAddMolActivity;
import com.example.inventoryui.Activities.Products.ProductsMainActivity;
import com.example.inventoryui.DataAccess.EmployeesData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Employee;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EmployeesMainActivity extends AppCompatActivity {

    final String TAG="MyActivity_EmployeeMain";
    FloatingActionButton addFabEmployee;
    RecyclerView employeesRecyclerView ;
    EmployeesAdapter employeesAdapter;
    private EmployeesData employeesData;
    ArrayList<Employee> employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_main);



        addFabEmployee=findViewById(R.id.addFabEmployee);
        addFabEmployee.show();
        addFabEmployeeOnClick();

        employeesData= new ViewModelProvider(this).get(EmployeesData.class);

        employeesRecyclerView=findViewById(R.id.employeesRecyclerView);
        employeesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        employees=new ArrayList<>();
        employeesAdapter=new EmployeesAdapter(EmployeesMainActivity.this, employees, new EmployeesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Employee item) {
                Intent i = new Intent(EmployeesMainActivity.this, EmployeeAddActivity.class);
                i.putExtra("employeeForUpdate", item);
                startActivity(i);
            }
        });
        employeesRecyclerView.setAdapter(employeesAdapter);

        getEmployees();
    }

    private void getEmployees() {
        employeesData.getAllEmployeesForUser().observe(this, new Observer<ArrayList<Employee>>() {
            @Override
            public void onChanged(ArrayList<Employee> newEmployees) {
                employees.clear();
                employees.addAll(newEmployees);
                employeesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addFabEmployeeOnClick() {
        addFabEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeesMainActivity.this, AdminAddMolActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.employees_menu,menu);
        Log.i(TAG,"inflating employee menu" );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                ( (AuthenticationManager)this.getApplication()).logout();
                /*Intent i=new Intent(EmployeesMainActivity.this, MainActivity.class);
                startActivity(i);*/
                return true;
            case R.id.products:
                Intent toProducts=new Intent(EmployeesMainActivity.this, ProductsMainActivity.class);
                startActivity(toProducts);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthenticationManager.activityResumed();
        AuthenticationManager.setActiveActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AuthenticationManager.activityPaused();
        AuthenticationManager.setActiveActivity(null);
    }
}




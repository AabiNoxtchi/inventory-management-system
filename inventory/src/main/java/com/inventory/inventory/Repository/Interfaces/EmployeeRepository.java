package com.inventory.inventory.Repository.Interfaces;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.User.Employee;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee>{

}

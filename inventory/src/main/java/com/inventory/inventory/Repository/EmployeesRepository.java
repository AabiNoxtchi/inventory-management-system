package com.inventory.inventory.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory.Model.Employee;


public interface EmployeesRepository extends JpaRepository<Employee,Long>{

	List<Employee> findByUserId(Long id);

}

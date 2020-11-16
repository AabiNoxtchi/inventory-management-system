package com.inventory.inventory.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory.Model.AbstractUser;
import com.inventory.inventory.Model.Employee;


public interface EmployeesRepository extends JpaRepository<Employee,Long>{

	Optional<AbstractUser> findByUserName(String username);		
	List<Employee> findByUserId(Long id);	
	Boolean existsByUserName(String username);	
	Boolean existsByEmail(String email);
	
}

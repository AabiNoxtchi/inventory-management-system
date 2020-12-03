package com.inventory.inventory.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(ERole name);
	
}

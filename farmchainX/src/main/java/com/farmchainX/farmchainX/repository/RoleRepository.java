package com.farmchainX.farmchainX.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmchainX.farmchainX.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByRoleName(String roleName);
	
	boolean existsByRoleName(String roleName);
	

	

}
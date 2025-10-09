package com.farmchainX.farmchainX.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.farmchainX.farmchainX.model.Role;
import com.farmchainX.farmchainX.repository.RoleRepository;

@Component
public class DataSeeder implements CommandLineRunner {
	 private final RoleRepository roleRepository;

	    public DataSeeder(RoleRepository roleRepository) {
	        this.roleRepository = roleRepository;
	    }

	    @Override
	    public void run(String... args) throws Exception {
	    	//storing all the roles in role table before giving access to create a new user
	        String[] roles= {"ROLE_CONSUMER",
	        				 "ROLE_FARMER",
	        				 "ROLE_DISTRIBUTER",
	        				 "ROLE_RETAILER",
	        				 "ROLE_ADMIN"
	        };
	        for(String roleName:roles) {
	        	//check whether role present in table or not before adding to the role table
	        	if(!roleRepository.existsByRoleName(roleName)) {
	        	Role role=new Role();
	        	role.setRoleName(roleName);
	        	roleRepository.save(role);
	        	}
	        }
	        System.out.println("Role seeding completed successfully");
	    }
}
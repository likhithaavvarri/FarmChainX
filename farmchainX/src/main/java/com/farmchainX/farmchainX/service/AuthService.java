package com.farmchainX.farmchainX.service;
import com.farmchainX.farmchainX.dto.RegisterRequest;
import com.farmchainX.farmchainX.model.User;
import com.farmchainX.farmchainX.model.Role;
import com.farmchainX.farmchainX.repository.UserRepository;
import com.farmchainX.farmchainX.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email is already taken";
        }
        String chosenRole=request.getRole().toUpperCase();
        if(chosenRole.equals("ADMIN")) {
        	return "cannot register as ADMIN";
        }
        String roleName="ROLE_"+chosenRole;
        Role userRole = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"+roleName));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return "User is registered successfully!";
    }
}
package com.farmchainX.farmchainX.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmchainX.farmchainX.dto.AuthResponse;
import com.farmchainX.farmchainX.dto.LoginRequest;
import com.farmchainX.farmchainX.dto.RegisterRequest;
import com.farmchainX.farmchainX.model.Role;
import com.farmchainX.farmchainX.model.User;
import com.farmchainX.farmchainX.repository.RoleRepository;
import com.farmchainX.farmchainX.repository.UserRepository;
import com.farmchainX.farmchainX.Security.JwtUtil;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequest request) {
        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return "⚠️ Email already exists!";
            }

            String roleInput = request.getRole().toUpperCase();

            // prevent direct admin sign-up
            if (roleInput.equals("ADMIN") || roleInput.equals("ROLE_ADMIN")) {
                return "🚫 Cannot register as Admin!";
            }

            String chosenRole = roleInput.startsWith("ROLE_") ? roleInput : "ROLE_" + roleInput;

            // ✅ changed from findByRoleName → findByName
            Role userRole = roleRepository.findByName(chosenRole)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + chosenRole));

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(Set.of(userRole));

            userRepository.save(user);

            return "✅ User registered successfully as " + chosenRole + "!";

        } catch (RuntimeException e) {
            e.printStackTrace();
            return "❌ Registration failed: " + e.getMessage();
        }
    }

    public AuthResponse login(LoginRequest login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // ✅ changed from getRoleName → getName
        String role = user.getRoles().iterator().next().getName();

        String token = jwtUtil.generateToken(user.getEmail(), role, user.getId());

        return new AuthResponse(token, role, user.getEmail());
    }
}
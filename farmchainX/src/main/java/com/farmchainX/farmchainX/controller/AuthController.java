package com.farmchainX.farmchainX.controller;



import com.farmchainX.farmchainX.dto.RegisterRequest;
import com.farmchainX.farmchainX.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.farmchainX.farmchainX.dto.AuthResponse;
import com.farmchainX.farmchainX.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest register) {
        String result = authService.register(register);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest login){
		return ResponseEntity.ok(authService.login(login));
	}
}
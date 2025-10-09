package com.farmchainX.farmchainX.controller;



import com.farmchainX.farmchainX.dto.RegisterRequest;
import com.farmchainX.farmchainX.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
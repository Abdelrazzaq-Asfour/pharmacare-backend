package com.pharmacare.controller;

import com.pharmacare.dto.request.LoginRequest;
import com.pharmacare.dto.response.JwtAuthResponse;
import com.pharmacare.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user authentication and issuing JWT tokens.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        JwtAuthResponse response = authService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }
}
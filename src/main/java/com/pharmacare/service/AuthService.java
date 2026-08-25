package com.pharmacare.service;

import com.pharmacare.dto.request.LoginRequest;
import com.pharmacare.dto.response.JwtAuthResponse;

public interface AuthService {
    JwtAuthResponse authenticateUser(LoginRequest request);
}
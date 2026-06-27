package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.dto.request.LoginRequest;
import com.example.TripHive_Backend.dto.request.SignUpRequest;
import com.example.TripHive_Backend.dto.response.LoginResponse;
import com.example.TripHive_Backend.dto.response.RefreshTokenResponse;
import com.example.TripHive_Backend.dto.response.SignUpResponse;

public interface AuthService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    LoginResponse login(LoginRequest loginRequest);
    RefreshTokenResponse refreshToken(String refreshToken);
}

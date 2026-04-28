package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.LoginRequest;
import com.example.AirBnb_Clone.dto.request.SignUpRequest;
import com.example.AirBnb_Clone.dto.response.LoginResponse;
import com.example.AirBnb_Clone.dto.response.SignUpResponse;

public interface AuthService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    LoginResponse login(LoginRequest loginRequest);
}

package com.example.AirBnb_Clone.controller;

import com.example.AirBnb_Clone.dto.request.LoginRequest;
import com.example.AirBnb_Clone.dto.request.SignUpRequest;
import com.example.AirBnb_Clone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        return new ResponseEntity<>(authService.signUp(signUpRequest),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(authService.login(loginRequest),HttpStatus.OK);
    }
}

package com.example.TripHive_Backend.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

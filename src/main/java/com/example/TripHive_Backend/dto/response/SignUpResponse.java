package com.example.TripHive_Backend.dto.response;

import lombok.Data;

@Data
public class SignUpResponse {
    private Long id;
    private String email;
    private String name;
}
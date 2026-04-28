package com.example.AirBnb_Clone.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
}

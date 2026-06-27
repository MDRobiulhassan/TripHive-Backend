package com.example.TripHive_Backend.dto.response;

import com.example.TripHive_Backend.enums.Gender;
import lombok.Data;

@Data
public class GuestResponseDTO {
    private Long id;
    private String name;
    private Integer age;
    private Gender gender;
}
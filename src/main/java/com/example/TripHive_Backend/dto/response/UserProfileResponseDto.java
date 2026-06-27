package com.example.TripHive_Backend.dto.response;

import com.example.TripHive_Backend.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileResponseDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String email;
}

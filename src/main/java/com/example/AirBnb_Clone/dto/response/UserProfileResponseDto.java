package com.example.AirBnb_Clone.dto.response;

import com.example.AirBnb_Clone.enums.Gender;
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

package com.example.AirBnb_Clone.dto.request;

import com.example.AirBnb_Clone.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}

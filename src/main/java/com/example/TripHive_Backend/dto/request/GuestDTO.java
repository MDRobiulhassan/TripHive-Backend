package com.example.TripHive_Backend.dto.request;

import com.example.TripHive_Backend.enums.Gender;
import lombok.Data;

@Data
public class GuestDTO {
    private String name;
    private Integer age;
    private Gender gender;
}

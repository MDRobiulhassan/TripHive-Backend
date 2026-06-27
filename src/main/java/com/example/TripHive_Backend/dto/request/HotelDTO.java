package com.example.TripHive_Backend.dto.request;

import com.example.TripHive_Backend.entity.HotelContactInfo;
import lombok.Data;

import java.util.List;

@Data
public class HotelDTO {
    private String name;
    private String city;
    private List<String> photos;
    private List<String> amenities;
    private HotelContactInfo contactInfo;
    private Boolean active;
}

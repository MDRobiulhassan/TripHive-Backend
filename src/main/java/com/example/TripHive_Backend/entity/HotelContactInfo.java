package com.example.TripHive_Backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class HotelContactInfo {

    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}

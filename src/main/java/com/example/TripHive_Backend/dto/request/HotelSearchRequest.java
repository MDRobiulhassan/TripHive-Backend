package com.example.TripHive_Backend.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequest {
    private String city;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfRooms;

    private Integer page = 0;
    private Integer size = 10;
}

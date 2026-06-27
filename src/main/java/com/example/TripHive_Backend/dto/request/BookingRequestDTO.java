package com.example.TripHive_Backend.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequestDTO {
    private Long hotelId;
    private Long roomId;
    private Integer numberOfRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}

package com.example.AirBnb_Clone.dto.response;

import com.example.AirBnb_Clone.entity.Guest;
import com.example.AirBnb_Clone.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingResponseDTO {
    private Long id;
    private Integer numberOfRooms;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Set<Guest> guests;
    private BigDecimal amount;
}

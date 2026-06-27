package com.example.TripHive_Backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class RoomResponseDTO {

    private Long id;
    private String type;
    private BigDecimal basePrice;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<String> amenities;
    private List<String> photos;
    private Integer totalCount;
    private Integer capacity;
}

package com.example.TripHive_Backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomDTO {
    private String type;
    private BigDecimal basePrice;
    private List<String> amenities;
    private List<String> photos;
    private Integer totalCount;
    private Integer capacity;

}
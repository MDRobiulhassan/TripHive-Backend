package com.example.TripHive_Backend.dto.request;

import com.example.TripHive_Backend.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelPriceDTO {
    private Hotel hotel;
    private Double price;
}

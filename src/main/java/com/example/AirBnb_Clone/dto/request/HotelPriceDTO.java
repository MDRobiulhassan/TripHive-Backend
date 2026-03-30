package com.example.AirBnb_Clone.dto.request;

import com.example.AirBnb_Clone.entity.Hotel;
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

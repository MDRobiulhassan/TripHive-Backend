package com.example.TripHive_Backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoResponseDTO {
    private HotelResponseDTO hotel;
    private List<RoomResponseDTO> rooms;
}

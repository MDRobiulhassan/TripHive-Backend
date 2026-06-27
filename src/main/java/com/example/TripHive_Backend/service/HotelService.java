package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.dto.request.HotelDTO;
import com.example.TripHive_Backend.dto.response.HotelInfoResponseDTO;
import com.example.TripHive_Backend.dto.response.HotelResponseDTO;

import java.util.List;

public interface HotelService {
    HotelResponseDTO createHotel(HotelDTO hoteldto);
//    List<HotelResponseDTO> getAllHotels();
    HotelResponseDTO getHotelById(Long id);
    HotelResponseDTO updateHotel(HotelDTO hoteldto,Long id);
    void deleteHotelById(Long id);
    void activateHotel(Long id);
    HotelInfoResponseDTO getHotelInfoById(Long hotelId);
    List<HotelResponseDTO> getAllHotels();
}

package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.HotelDTO;
import com.example.AirBnb_Clone.dto.response.HotelInfoResponseDTO;
import com.example.AirBnb_Clone.dto.response.HotelResponseDTO;
import com.example.AirBnb_Clone.entity.Hotel;
import org.jspecify.annotations.Nullable;

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

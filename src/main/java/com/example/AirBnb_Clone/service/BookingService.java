package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.BookingRequestDTO;
import com.example.AirBnb_Clone.dto.request.GuestDTO;
import com.example.AirBnb_Clone.dto.response.BookingResponseDTO;
import com.example.AirBnb_Clone.dto.response.GuestResponseDTO;

import java.util.List;

public interface BookingService {
    BookingResponseDTO initialiseBooking(BookingRequestDTO bookingRequestDTO);

    List<GuestResponseDTO> addGuests(Long bookingId, List<GuestDTO> guestDTO);
}

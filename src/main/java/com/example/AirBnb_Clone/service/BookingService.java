package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.BookingRequestDTO;
import com.example.AirBnb_Clone.dto.request.GuestDTO;
import com.example.AirBnb_Clone.dto.response.BookingResponseDTO;
import com.example.AirBnb_Clone.dto.response.GuestResponseDTO;
import com.stripe.model.Event;

import java.util.List;
import java.util.Map;

public interface BookingService {
    BookingResponseDTO initialiseBooking(BookingRequestDTO bookingRequestDTO);

    List<GuestResponseDTO> addGuests(Long bookingId, List<GuestDTO> guestDTO);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}

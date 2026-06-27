package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.dto.request.BookingRequestDTO;
import com.example.TripHive_Backend.dto.request.GuestDTO;
import com.example.TripHive_Backend.dto.response.BookingResponseDTO;
import com.example.TripHive_Backend.dto.response.GuestResponseDTO;
import com.example.TripHive_Backend.dto.response.HotelReportResponse;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingResponseDTO initialiseBooking(BookingRequestDTO bookingRequestDTO);

    List<GuestResponseDTO> addGuests(Long bookingId, List<GuestDTO> guestDTO);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingResponseDTO> getAllBookingsByHotel(Long hotelId);

    HotelReportResponse getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingResponseDTO> getMyBookings();
}

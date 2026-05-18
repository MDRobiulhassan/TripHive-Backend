package com.example.AirBnb_Clone.controller;

import com.example.AirBnb_Clone.dto.request.BookingRequestDTO;
import com.example.AirBnb_Clone.dto.request.GuestDTO;
import com.example.AirBnb_Clone.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<?> initialiseBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.initialiseBooking(bookingRequestDTO));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<?> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDTO> guestDTO) {
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDTO));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<?> initiatePayment(@PathVariable Long bookingId) {
        String sessionUrl = bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookingId}/status")
    public ResponseEntity<?> getBookingStatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(Map.of("status", bookingService.getBookingStatus(bookingId)));
    }
}

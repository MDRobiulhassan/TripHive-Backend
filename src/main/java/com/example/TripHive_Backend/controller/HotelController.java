package com.example.TripHive_Backend.controller;

import com.example.TripHive_Backend.dto.request.HotelDTO;
import com.example.TripHive_Backend.service.BookingService;
import com.example.TripHive_Backend.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createHotel(@RequestBody HotelDTO hotelDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(hotelService.createHotel(hotelDTO));
    }

//    @GetMapping
//    public ResponseEntity<?> getAllHotels() {
//        return ResponseEntity.ok(hotelService.getAllHotels());
//    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<?> getHotelById(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<?> updateHotel(@PathVariable Long hotelId, @RequestBody HotelDTO hotelDTO) {
        return ResponseEntity.ok(hotelService.updateHotel(hotelDTO, hotelId));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<?> deleteHotelById(@PathVariable Long hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<?> activateHotel(@PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<?> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<?> getAllBookingsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotel(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    public ResponseEntity<?> getHotelReport(@PathVariable Long hotelId,
                                            @RequestParam(required = false) LocalDate startDate,
                                            @RequestParam(required = false) LocalDate endDate) {
        if(startDate==null) startDate = LocalDate.now().minusMonths(1);
        if(endDate==null) endDate = LocalDate.now();
        return ResponseEntity.ok(bookingService.getHotelReport(hotelId,startDate,endDate));
    }
}

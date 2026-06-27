package com.example.TripHive_Backend.controller;

import com.example.TripHive_Backend.dto.request.HotelPriceDTO;
import com.example.TripHive_Backend.dto.request.HotelSearchRequest;
import com.example.TripHive_Backend.service.HotelService;
import com.example.TripHive_Backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelBrowserController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @PostMapping("/search")
    public ResponseEntity<?> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {
        Page<HotelPriceDTO> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("{hotelId}/info")
    public ResponseEntity<?> getHotelDetails(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}

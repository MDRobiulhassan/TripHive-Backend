package com.example.AirBnb_Clone.controller;

import com.example.AirBnb_Clone.dto.request.HotelPriceDTO;
import com.example.AirBnb_Clone.dto.request.HotelSearchRequest;
import com.example.AirBnb_Clone.dto.response.HotelInfoResponseDTO;
import com.example.AirBnb_Clone.dto.response.HotelResponseDTO;
import com.example.AirBnb_Clone.service.HotelService;
import com.example.AirBnb_Clone.service.InventoryService;
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

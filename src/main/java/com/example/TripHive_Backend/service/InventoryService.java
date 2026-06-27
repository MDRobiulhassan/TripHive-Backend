package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.dto.request.HotelPriceDTO;
import com.example.TripHive_Backend.dto.request.HotelSearchRequest;
import com.example.TripHive_Backend.dto.request.UpdateInventoryRequestDto;
import com.example.TripHive_Backend.dto.response.InventoryResponse;
import com.example.TripHive_Backend.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryResponse> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}

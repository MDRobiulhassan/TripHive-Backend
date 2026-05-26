package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.HotelPriceDTO;
import com.example.AirBnb_Clone.dto.request.HotelSearchRequest;
import com.example.AirBnb_Clone.dto.request.UpdateInventoryRequestDto;
import com.example.AirBnb_Clone.dto.response.InventoryResponse;
import com.example.AirBnb_Clone.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest);

    List<InventoryResponse> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}

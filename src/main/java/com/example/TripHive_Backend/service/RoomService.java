package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.dto.request.RoomDTO;
import com.example.TripHive_Backend.dto.response.RoomResponseDTO;

import java.util.List;

public interface RoomService {
    RoomResponseDTO createRoom(Long hotelId,RoomDTO roomDTO);
    List<RoomResponseDTO> getAllRoomsInHotel(Long hotelId);
    RoomResponseDTO getRoomById(Long id);
    void deleteRoomById(Long id);

    RoomResponseDTO updateRoomById(Long hotelId, Long roomId, RoomDTO roomDTO);
}

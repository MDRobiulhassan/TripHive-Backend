package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.RoomDTO;
import com.example.AirBnb_Clone.dto.response.RoomResponseDTO;
import com.example.AirBnb_Clone.entity.Room;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface RoomService {
    RoomResponseDTO createRoom(Long hotelId,RoomDTO roomDTO);
    List<RoomResponseDTO> getAllRoomsInHotel(Long hotelId);
    RoomResponseDTO getRoomById(Long id);
    void deleteRoomById(Long id);

    RoomResponseDTO updateRoomById(Long hotelId, Long roomId, RoomDTO roomDTO);
}

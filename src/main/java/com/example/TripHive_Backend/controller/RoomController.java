package com.example.TripHive_Backend.controller;

import com.example.TripHive_Backend.dto.request.RoomDTO;
import com.example.TripHive_Backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels/{hotelId}/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@PathVariable Long hotelId, @RequestBody RoomDTO roomDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.createRoom(hotelId, roomDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllRoomsInHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable Long roomId, @PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long roomId, @PathVariable Long hotelId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoomById(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId, roomId, roomDTO));
    }
}

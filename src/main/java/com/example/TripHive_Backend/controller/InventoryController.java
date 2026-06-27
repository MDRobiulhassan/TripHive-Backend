package com.example.TripHive_Backend.controller;

import com.example.TripHive_Backend.dto.request.UpdateInventoryRequestDto;
import com.example.TripHive_Backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<?> getAllInventoryByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(inventoryService.getAllInventoryByRoom(roomId));
    }

    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<?> updateInventory(@PathVariable Long roomId, @RequestBody UpdateInventoryRequestDto updateInventoryRequestDto) {
        inventoryService.updateInventory(roomId, updateInventoryRequestDto);
        return ResponseEntity.noContent().build();
    }
}

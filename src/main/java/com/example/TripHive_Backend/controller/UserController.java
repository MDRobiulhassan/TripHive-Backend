package com.example.TripHive_Backend.controller;

import com.example.TripHive_Backend.dto.request.ProfileUpdateRequestDto;
import com.example.TripHive_Backend.service.BookingService;
import com.example.TripHive_Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) {
        userService.updateProfile(profileUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    public ResponseEntity<?> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
}

package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.dto.request.ProfileUpdateRequestDto;
import com.example.TripHive_Backend.dto.response.UserProfileResponseDto;
import com.example.TripHive_Backend.entity.User;

public interface UserService {
    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserProfileResponseDto getUserProfile();
}

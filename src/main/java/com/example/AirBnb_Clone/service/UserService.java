package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.dto.request.ProfileUpdateRequestDto;
import com.example.AirBnb_Clone.dto.response.UserProfileResponseDto;
import com.example.AirBnb_Clone.entity.User;
import org.jspecify.annotations.Nullable;

public interface UserService {
    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserProfileResponseDto getUserProfile();
}

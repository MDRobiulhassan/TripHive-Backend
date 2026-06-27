package com.example.TripHive_Backend.serviceImpl;

import com.example.TripHive_Backend.dto.request.ProfileUpdateRequestDto;
import com.example.TripHive_Backend.dto.response.UserProfileResponseDto;
import com.example.TripHive_Backend.entity.User;
import com.example.TripHive_Backend.exceptions.ResourceNotFoundException;
import com.example.TripHive_Backend.repository.UserRepository;
import com.example.TripHive_Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.TripHive_Backend.util.AppUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = getCurrentUser();

        if (profileUpdateRequestDto.getDateOfBirth() != null)
            user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
        if (profileUpdateRequestDto.getGender() != null) user.setGender(profileUpdateRequestDto.getGender());
        if (profileUpdateRequestDto.getName() != null) user.setName(profileUpdateRequestDto.getName());

        userRepository.save(user);
    }

    @Override
    public UserProfileResponseDto getUserProfile() {
        User user = getCurrentUser();
        log.info("Fetching profile for user: {}", user.getEmail());
        return modelMapper.map(user, UserProfileResponseDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}

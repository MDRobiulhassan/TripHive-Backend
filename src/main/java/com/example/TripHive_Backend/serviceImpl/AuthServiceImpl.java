package com.example.TripHive_Backend.serviceImpl;

import com.example.TripHive_Backend.dto.request.LoginRequest;
import com.example.TripHive_Backend.dto.request.SignUpRequest;
import com.example.TripHive_Backend.dto.response.LoginResponse;
import com.example.TripHive_Backend.dto.response.RefreshTokenResponse;
import com.example.TripHive_Backend.dto.response.SignUpResponse;
import com.example.TripHive_Backend.entity.User;
import com.example.TripHive_Backend.enums.Roles;
import com.example.TripHive_Backend.exceptions.ResourceNotFoundException;
import com.example.TripHive_Backend.repository.UserRepository;
import com.example.TripHive_Backend.security.JwtService;
import com.example.TripHive_Backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        User user = userRepository.findByEmail(signUpRequest.getEmail()).orElse(null);

        if (user != null) {
            throw new RuntimeException("Email already registered");
        }

        User newUser = modelMapper.map(signUpRequest, User.class);
        newUser.setRoles(Set.of(Roles.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        newUser = userRepository.save(newUser);
        return modelMapper.map(newUser, SignUpResponse.class);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ));

        User user = (User) authentication.getPrincipal();

        assert user != null;
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new RefreshTokenResponse(jwtService.generateAccessToken(user));
    }
}

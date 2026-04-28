package com.example.AirBnb_Clone.serviceImpl;

import com.example.AirBnb_Clone.dto.request.LoginRequest;
import com.example.AirBnb_Clone.dto.request.SignUpRequest;
import com.example.AirBnb_Clone.dto.response.LoginResponse;
import com.example.AirBnb_Clone.dto.response.SignUpResponse;
import com.example.AirBnb_Clone.entity.User;
import com.example.AirBnb_Clone.entity.enums.Roles;
import com.example.AirBnb_Clone.repository.UserRepository;
import com.example.AirBnb_Clone.security.JwtService;
import com.example.AirBnb_Clone.service.AuthService;
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
}

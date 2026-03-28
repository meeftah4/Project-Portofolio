package com.aicvbuilder.service;

import com.aicvbuilder.dto.AuthRequest;
import com.aicvbuilder.dto.AuthResponse;
import com.aicvbuilder.entity.User;
import com.aicvbuilder.repository.UserRepository;
import com.aicvbuilder.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthResponse register(AuthRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("Email already registered")
                    .build();
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userId(user.getId())
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = tokenProvider.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .userId(user.getId())
                .message("Login successful")
                .build();
    }
}

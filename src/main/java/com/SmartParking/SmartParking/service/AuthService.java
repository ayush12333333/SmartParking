package com.SmartParking.SmartParking.service;

import com.SmartParking.SmartParking.auth.AuthRequest;
import com.SmartParking.SmartParking.auth.AuthResponse;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.entity.UserRole;
import com.SmartParking.SmartParking.repository.UserRepository;
import com.SmartParking.SmartParking.utilis.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // continue with saving user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public ResponseEntity<?> login(AuthRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Please signup first"));
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "Invalid password"));
        }

        String token = jwtService.generateToken(user.getEmail(),user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}

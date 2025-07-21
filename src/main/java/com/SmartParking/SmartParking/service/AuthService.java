package com.SmartParking.SmartParking.service;

import com.SmartParking.SmartParking.auth.AuthRequest;
import com.SmartParking.SmartParking.auth.AuthResponse;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.entity.UserRole;
import com.SmartParking.SmartParking.repository.UserRepository;
import com.SmartParking.SmartParking.utilis.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);  // Prevent role tampering
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}

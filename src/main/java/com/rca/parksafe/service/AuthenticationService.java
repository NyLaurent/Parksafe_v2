package com.rca.parksafe.service;

import com.rca.parksafe.config.JwtService;
import com.rca.parksafe.dto.AuthResponse;
import com.rca.parksafe.dto.RegisterRequest;
import com.rca.parksafe.entity.Role;
import com.rca.parksafe.entity.User;
import com.rca.parksafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByUsername(request.getUsername())) {
                        return AuthResponse.builder()
                                        .message("Username already exists")
                                        .build();
                }

                if (userRepository.existsByEmail(request.getEmail())) {
                        return AuthResponse.builder()
                                        .message("Email already exists")
                                        .build();
                }

                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(Role.USER);

                userRepository.save(user);
                var jwtToken = jwtService.generateToken(user);

                return AuthResponse.builder()
                                .token(jwtToken)
                                .username(user.getUsername())
                                .message("Registration successful")
                                .build();
        }

        public AuthResponse authenticate(String username, String password) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(username, password));

                var user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                var jwtToken = jwtService.generateToken(user);

                return AuthResponse.builder()
                                .token(jwtToken)
                                .username(user.getUsername())
                                .message("Login successful")
                                .build();
        }
}
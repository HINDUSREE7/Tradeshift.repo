package com.example.sample.service;

import com.example.sample.dto.AuthRequest;
import com.example.sample.dto.AuthResponse;
import com.example.sample.dto.RegisterRequest;
import com.example.sample.entity.User;
import com.example.sample.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserService userService,
                       JwtService jwtService, AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if user exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        userRepository.save(user);

        // Generate token
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(jwt);
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Generate token
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String jwt = jwtService.generateToken(userDetails);

        return new AuthResponse(jwt);
    }
}
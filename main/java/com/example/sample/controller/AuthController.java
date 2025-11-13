package com.example.sample.controller;

import com.example.sample.entity.User;
import com.example.sample.service.UserService;
import com.example.sample.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@SuppressWarnings("unused")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");

            // Validation
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Check if user exists
            if (userService.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            // Check if email exists
            if (email != null && !email.trim().isEmpty()) {
                Optional<User> existingEmail = userService.findByEmail(email);
                if (existingEmail.isPresent()) {
                    return ResponseEntity.badRequest().body("Email already exists");
                }
            }

            // Create and save user
            User user = new User();
            user.setUsername(username.trim());
            user.setPassword(passwordEncoder.encode(password.trim()));
            user.setEmail(email != null ? email.trim() : null);

            User savedUser = userService.save(user);

            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("id", savedUser.getId());
            response.put("username", savedUser.getUsername());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Find user
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }

            User user = userOpt.get();

            // Check password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid password");
            }

            // Generate JWT token
            String jwt = jwtUtil.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", username);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}
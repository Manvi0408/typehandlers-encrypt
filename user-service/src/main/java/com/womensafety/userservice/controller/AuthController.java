package com.womensafety.userservice.controller;

import com.womensafety.userservice.dto.*;
import com.womensafety.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            AuthResponseDto response = authService.register(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Registration failed for user: {}", registrationDto.getUsername(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginDto loginDto) {
        try {
            AuthResponseDto response = authService.login(loginDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginDto.getUsernameOrEmail(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                throw new RuntimeException("Refresh token is required");
            }
            
            AuthResponseDto response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new RuntimeException("Token refresh failed: " + e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email address")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        try {
            authService.verifyEmail(token);
            Map<String, String> response = Map.of(
                "message", "Email verified successfully",
                "status", "success"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Email verification failed for token: {}", token, e);
            Map<String, String> response = Map.of(
                "message", "Email verification failed: " + e.getMessage(),
                "status", "error"
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }
            
            authService.forgotPassword(email);
            Map<String, String> response = Map.of(
                "message", "Password reset email sent successfully",
                "status", "success"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Forgot password failed", e);
            Map<String, String> response = Map.of(
                "message", "Password reset request failed: " + e.getMessage(),
                "status", "error"
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            
            if (token == null || token.trim().isEmpty()) {
                throw new RuntimeException("Token is required");
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new RuntimeException("New password is required");
            }
            
            authService.resetPassword(token, newPassword);
            Map<String, String> response = Map.of(
                "message", "Password reset successfully",
                "status", "success"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password reset failed", e);
            Map<String, String> response = Map.of(
                "message", "Password reset failed: " + e.getMessage(),
                "status", "error"
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = Map.of(
            "status", "UP",
            "service", "User Service",
            "timestamp", java.time.Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}
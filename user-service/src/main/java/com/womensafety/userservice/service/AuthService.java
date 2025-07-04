package com.womensafety.userservice.service;

import com.womensafety.userservice.dto.*;
import com.womensafety.userservice.entity.User;
import com.womensafety.userservice.entity.UserRole;
import com.womensafety.userservice.repository.UserRepository;
import com.womensafety.userservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${security.account.max-login-attempts}")
    private int maxLoginAttempts;

    @Value("${security.account.lockout-duration}")
    private long lockoutDuration;

    @Value("${security.account.verification-token-expiry}")
    private long verificationTokenExpiry;

    @Transactional
    public AuthResponseDto register(UserRegistrationDto registrationDto) {
        log.info("Attempting to register user: {}", registrationDto.getUsername());

        // Check if user already exists
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Validate password strength
        validatePasswordStrength(registrationDto.getPassword());

        // Create new user
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setDateOfBirth(registrationDto.getDateOfBirth());
        user.setIsActive(true);
        user.setIsVerified(false);

        // Generate email verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        user.setEmailVerificationExpiry(LocalDateTime.now().plusSeconds(verificationTokenExpiry / 1000));

        // Save user
        user = userRepository.save(user);

        // Assign default USER role
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(UserRole.Role.USER);
        userRoleRepository.save(userRole);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), verificationToken);

        log.info("User registered successfully: {}", user.getUsername());

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserResponseDto userResponse = convertToUserResponse(user);
        return new AuthResponseDto(accessToken, refreshToken, jwtService.getExpirationTime(), userResponse);
    }

    @Transactional
    public AuthResponseDto login(UserLoginDto loginDto) {
        log.info("Attempting to login user: {}", loginDto.getUsernameOrEmail());

        // Find user by username or email
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(
                loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = optionalUser.get();

        // Check if account is locked
        if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Account is locked. Please try again later.");
        }

        // Check if account is active
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        // Validate password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
            handleFailedLogin(user);
            throw new RuntimeException("Invalid credentials");
        }

        // Reset failed login attempts on successful login
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        log.info("User logged in successfully: {}", user.getUsername());

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserResponseDto userResponse = convertToUserResponse(user);
        return new AuthResponseDto(accessToken, refreshToken, jwtService.getExpirationTime(), userResponse);
    }

    @Transactional
    public AuthResponseDto refreshToken(String refreshToken) {
        log.info("Attempting to refresh token");

        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtService.getUsernameFromToken(refreshToken);
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        UserResponseDto userResponse = convertToUserResponse(user);
        return new AuthResponseDto(newAccessToken, newRefreshToken, jwtService.getExpirationTime(), userResponse);
    }

    @Transactional
    public void verifyEmail(String token) {
        log.info("Attempting to verify email with token: {}", token);

        Optional<User> optionalUser = userRepository.findByEmailVerificationToken(token);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid verification token");
        }

        User user = optionalUser.get();

        if (user.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        user.setIsVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiry(null);
        userRepository.save(user);

        log.info("Email verified successfully for user: {}", user.getUsername());
    }

    @Transactional
    public void forgotPassword(String email) {
        log.info("Attempting forgot password for email: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            // Don't reveal that email doesn't exist
            log.warn("Password reset requested for non-existent email: {}", email);
            return;
        }

        User user = optionalUser.get();

        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(1)); // 1 hour expiry

        userRepository.save(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetToken);

        log.info("Password reset email sent to: {}", email);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        log.info("Attempting to reset password with token: {}", token);

        Optional<User> optionalUser = userRepository.findByPasswordResetToken(token);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid password reset token");
        }

        User user = optionalUser.get();

        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired");
        }

        // Validate new password
        validatePasswordStrength(newPassword);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getUsername());
    }

    private void handleFailedLogin(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

        if (user.getFailedLoginAttempts() >= maxLoginAttempts) {
            user.setAccountLockedUntil(LocalDateTime.now().plusSeconds(lockoutDuration / 1000));
            log.warn("Account locked for user: {}", user.getUsername());
        }

        userRepository.save(user);
    }

    private void validatePasswordStrength(String password) {
        if (password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new RuntimeException("Password must contain at least one special character");
        }
    }

    private UserResponseDto convertToUserResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setIsActive(user.getIsActive());
        response.setIsVerified(user.getIsVerified());
        response.setLastLogin(user.getLastLogin());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        // Get user roles
        List<String> roles = userRoleRepository.findByUserId(user.getId())
                .stream()
                .map(role -> role.getRole().getValue())
                .collect(Collectors.toList());
        response.setRoles(roles);

        return response;
    }
}
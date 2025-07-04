package com.womensafety.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.verification.subject}")
    private String verificationSubject;

    @Value("${email.verification.from}")
    private String verificationFrom;

    @Value("${email.password-reset.subject}")
    private String passwordResetSubject;

    @Value("${email.password-reset.from}")
    private String passwordResetFrom;

    public void sendVerificationEmail(String toEmail, String firstName, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(verificationFrom);
            message.setTo(toEmail);
            message.setSubject(verificationSubject);
            message.setText(buildVerificationEmailContent(firstName, token));

            mailSender.send(message);
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String firstName, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(passwordResetFrom);
            message.setTo(toEmail);
            message.setSubject(passwordResetSubject);
            message.setText(buildPasswordResetEmailContent(firstName, token));

            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private String buildVerificationEmailContent(String firstName, String token) {
        return String.format(
            "Dear %s,\n\n" +
            "Welcome to the Women Safety Platform!\n\n" +
            "Please click the link below to verify your email address:\n" +
            "%s/api/auth/verify-email?token=%s\n\n" +
            "If you did not create an account, please ignore this email.\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "Best regards,\n" +
            "Women Safety Platform Team",
            firstName,
            getBaseUrl(),
            token
        );
    }

    private String buildPasswordResetEmailContent(String firstName, String token) {
        return String.format(
            "Dear %s,\n\n" +
            "You have requested to reset your password for your Women Safety Platform account.\n\n" +
            "Please click the link below to reset your password:\n" +
            "%s/reset-password?token=%s\n\n" +
            "If you did not request a password reset, please ignore this email.\n\n" +
            "This link will expire in 1 hour.\n\n" +
            "Best regards,\n" +
            "Women Safety Platform Team",
            firstName,
            getBaseUrl(),
            token
        );
    }

    private String getBaseUrl() {
        // In production, this should be configurable
        return "https://womensafety.com";
    }
}
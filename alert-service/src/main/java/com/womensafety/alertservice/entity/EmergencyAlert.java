package com.womensafety.alertservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emergency_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    @NotNull(message = "Alert type is required")
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Severity is required")
    private Severity severity;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.ACTIVE;

    @Column(name = "is_false_alarm")
    private Boolean isFalseAlarm = false;

    @Column(name = "escalation_level")
    private Integer escalationLevel = 0;

    @Column(name = "escalated_at")
    private LocalDateTime escalatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AlertResponse> responses;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AlertType {
        SOS("SOS"),
        HARASSMENT("HARASSMENT"),
        ASSAULT("ASSAULT"),
        STALKING("STALKING"),
        MEDICAL_EMERGENCY("MEDICAL_EMERGENCY"),
        FIRE("FIRE"),
        ACCIDENT("ACCIDENT"),
        SUSPICIOUS_ACTIVITY("SUSPICIOUS_ACTIVITY"),
        OTHER("OTHER");

        private final String value;

        AlertType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Severity {
        CRITICAL("CRITICAL"),
        HIGH("HIGH"),
        MEDIUM("MEDIUM"),
        LOW("LOW");

        private final String value;

        Severity(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum AlertStatus {
        ACTIVE("ACTIVE"),
        ACKNOWLEDGED("ACKNOWLEDGED"),
        IN_PROGRESS("IN_PROGRESS"),
        RESOLVED("RESOLVED"),
        CANCELLED("CANCELLED"),
        FALSE_ALARM("FALSE_ALARM");

        private final String value;

        AlertStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Utility methods
    public boolean isActive() {
        return status == AlertStatus.ACTIVE || status == AlertStatus.ACKNOWLEDGED || status == AlertStatus.IN_PROGRESS;
    }

    public boolean canBeEscalated() {
        return isActive() && escalationLevel < 3;
    }

    public boolean isResolved() {
        return status == AlertStatus.RESOLVED || status == AlertStatus.CANCELLED || status == AlertStatus.FALSE_ALARM;
    }
}
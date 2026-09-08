package com.example.resourcemanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.resourcemanagement.enums.ReservationStatus;

public class ReservationResponse {

    private Long id;
    private Long resourceId;
    private String resourceName;
    private String userEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private ReservationStatus status;

    // Constructor
    public ReservationResponse(
            Long id,
            Long resourceId,
            String resourceName,
            String userEmail,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal price,
            ReservationStatus status) {

        this.id = id;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.userEmail = userEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
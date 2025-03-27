package com.orderrideservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_ride_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRideHistory {

    @EmbeddedId
    private UserRideHistoryId id;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserRideHistory(UUID userId, UUID rideId) {
        this.id = new UserRideHistoryId(userId, rideId);
    }
}
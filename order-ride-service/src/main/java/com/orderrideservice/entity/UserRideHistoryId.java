package com.orderrideservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Составной идентификатор для истории поездок пользователя.
 * Содержит пару user_id и ride_id как первичный ключ.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRideHistoryId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "ride_id", nullable = false)
    private UUID rideId;

}
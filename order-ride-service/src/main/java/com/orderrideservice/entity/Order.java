package com.orderrideservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сущность заказа.
 * Представляет заказ в системе с информацией о пользователе, водителе и платеже.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Column(name = "payment_id", nullable = true)
    private UUID paymentId;

    @Column(name = "driver_id", nullable = true)
    private UUID driverId;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

}

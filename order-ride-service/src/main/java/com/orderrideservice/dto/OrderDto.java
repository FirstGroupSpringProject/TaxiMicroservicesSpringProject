package com.orderrideservice.dto;

import lombok.Data;
import com.orderrideservice.entity.OrderStatus;

import java.util.UUID;

/**
 * DTO для представления данных заказа.
 * Используется для передачи информации о заказе между слоями приложения.
 */
@Data
public class OrderDto {
    private UUID id;
    private UUID userId;
    private UUID driverId;
    private UUID paymentId;
    private String pickupAddress;
    private String dropAddress;
    private OrderStatus status;
}
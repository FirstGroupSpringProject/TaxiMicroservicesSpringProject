package com.orderrideservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Событие обновления статуса платежа для заказа.
 * Содержит информацию о заказе, платеже и его новом статусе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentUpdateEvent {
    private UUID orderId;
    private UUID paymentId;
    private String paymentStatus;
}
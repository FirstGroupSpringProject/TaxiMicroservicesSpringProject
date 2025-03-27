package com.aston.paymentbillingservice.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Событие обновления статуса платежа.
 * Содержит полную информацию о платеже и его текущем статусе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusUpdatedEvent {
    private UUID paymentId;
    private UUID orderId;
    private UUID userId;
    private BigDecimal amount;
    private String status;

}
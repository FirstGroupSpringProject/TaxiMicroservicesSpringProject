package com.aston.commonevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

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
package com.orderrideservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideCompletedEvent {
    private UUID rideId;
    private UUID orderId;
    private UUID driverId;
    private UUID userId;
    private BigDecimal amount;

}
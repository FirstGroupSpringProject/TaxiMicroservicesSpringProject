package com.orderrideservice.dto;

import lombok.Data;
import com.orderrideservice.entity.OrderStatus;

import java.util.UUID;

@Data
public class OrderDto {

    private UUID id;

    private UUID userId;

    private String pickupAddress;

    private OrderStatus status;

}

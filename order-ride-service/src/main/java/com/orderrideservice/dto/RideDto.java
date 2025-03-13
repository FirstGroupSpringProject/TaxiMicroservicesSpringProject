package com.orderrideservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RideDto {

    private UUID id;

    private UUID orderId;

    private UUID driverId;

    private Double distance;

}

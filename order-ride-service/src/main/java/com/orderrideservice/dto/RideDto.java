package com.orderrideservice.dto;

import lombok.Data;

import java.util.UUID;

/**
 * DTO для представления данных поездки.
 * Содержит основную информацию о поездке.
 */
@Data
public class RideDto {

    private UUID id;

    private UUID orderId;

    private UUID driverId;

    private Double distance;

}

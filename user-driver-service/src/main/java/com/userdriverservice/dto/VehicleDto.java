package com.userdriverservice.dto;

import lombok.Data;

import java.util.UUID;
/**
 * DTO для представления данных транспортного средства.
 * Используется для передачи информации о транспортных средствах между слоями приложения.
 */
@Data
public class VehicleDto {

    private UUID id;

    private UUID driverId;

    private String model;

    private String number;

}

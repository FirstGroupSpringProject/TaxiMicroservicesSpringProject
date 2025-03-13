package com.userdriverservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VehicleDto {

    private UUID id;

    private UUID driverId;

    private String model;

    private String number;

}

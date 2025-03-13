package com.userdriverservice.dto;

import com.userdriverservice.entity.DriverStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class DriverDto {

    private UUID id;

    private String name;

    private Integer ordersCompleted;

    private DriverStatus currentStatus;

}

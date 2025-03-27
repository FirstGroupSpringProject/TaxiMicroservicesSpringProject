package com.userdriverservice.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class DriverDto {
    private UUID id;
    private String name;
    private Integer ordersCompleted;
    private String currentStatus;

}
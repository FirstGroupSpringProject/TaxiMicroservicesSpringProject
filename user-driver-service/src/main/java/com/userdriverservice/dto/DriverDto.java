package com.userdriverservice.dto;


import lombok.Data;

import java.util.UUID;
/**
 * DTO для представления данных водителя.
 * Используется для передачи информации о водителе между слоями приложения.
 */
@Data
public class DriverDto {
    private UUID id;
    private String name;
    private Integer ordersCompleted;
    private String currentStatus;

}
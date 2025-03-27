package com.aston.commonevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * Содержит информацию о водителе, его статусе и активности.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEventPayload {
    private UUID driverId;
    private String name;
    private Integer ordersCompleted;
    private String currentStatus;
    private boolean active;
}
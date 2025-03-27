package com.userdriverservice.event;

import com.userdriverservice.dto.DriverDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Событие, связанное с водителем.
 * Содержит информацию о событии изменения данных водителя.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverEvent {
    private UUID driverId;
    private String eventType;
    private DriverDto driverData;


}
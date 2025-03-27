package com.aston.commonevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Событие, связанное с водителем.
 * Содержит тип события и данные, связанные с этим событием.
 *
 * @see DriverEventPayload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEvent {
    private String eventType;
    private DriverEventPayload payload;
}
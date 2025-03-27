package com.aston.commonevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEvent {
    private String eventType;
    private DriverEventPayload payload;
}
package com.orderrideservice.event;

import com.orderrideservice.dto.RideDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideEvent {
    private UUID rideId;
    private String eventType;
    private RideDto rideData;

}

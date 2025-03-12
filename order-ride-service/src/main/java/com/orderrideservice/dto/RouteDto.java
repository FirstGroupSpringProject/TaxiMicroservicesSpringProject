package com.orderrideservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class RouteDto {

    private UUID id;

    private UUID rideId;

    private Date startTime;

    private Date endTime;

}

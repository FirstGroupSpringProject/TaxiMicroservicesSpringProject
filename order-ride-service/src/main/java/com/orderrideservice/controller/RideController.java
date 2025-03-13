package com.orderrideservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.service.RideService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;
    private static final Logger LOG = LoggerFactory.getLogger(RideController.class);

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<RideDto> createRide(@RequestBody RideDto rideDto) {
        RideDto createdRide = rideService.createRide(rideDto);
        return ResponseEntity.ok(createdRide);
    }

    @GetMapping
    public ResponseEntity<List<RideDto>> getRides() {
        List<RideDto> rideDtoList = rideService.getAllRides();
        if (rideDtoList.isEmpty()) {
            LOG.info("rideDtoList if empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            LOG.info("rideDtoList is {}", rideDtoList);
            return ResponseEntity.ok().body(rideDtoList);
        }
    }
}

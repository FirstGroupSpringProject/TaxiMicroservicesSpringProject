package com.userdriverservice.controller;

import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;
    private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<DriverDto> createDriver(@RequestBody DriverDto driverDto) {
        DriverDto createdDriver = driverService.createDriver(driverDto);
        return ResponseEntity.ok(createdDriver);
    }

    @GetMapping
    public ResponseEntity<List<DriverDto>> getDrivers() {
        List<DriverDto> driverDtoList = driverService.getAllDrivers();
        if (driverDtoList == null || driverDtoList.isEmpty()) {
            LOG.info("No drivers found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            LOG.info("Drivers found: {}", driverDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(driverDtoList);
        }
    }
}
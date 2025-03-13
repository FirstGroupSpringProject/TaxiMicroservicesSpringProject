package com.userdriverservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.userdriverservice.dto.VehicleDto;
import com.userdriverservice.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@RequestBody VehicleDto vehicleDto) {
        VehicleDto createdVehicle = vehicleService.createVehicle(vehicleDto);
        return ResponseEntity.ok().body(createdVehicle);
    }

    @GetMapping("/{number}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable String number) {
        VehicleDto vehicleDto = vehicleService.getVehicleByNumber(number);
        return ResponseEntity.ok().body(vehicleDto);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDto>> getVehicles() {
        List<VehicleDto> vehicleDtoList = vehicleService.getAllVehicles();
        if (vehicleDtoList.isEmpty()) {
            LOG.info("vehicleDtoList if empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            LOG.info("vehicleDtoList is {}", vehicleDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(vehicleDtoList);
        }
    }
}

package com.userdriverservice.controller;

import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);

    @PostMapping
    public ResponseEntity<DriverDto> createDriver(@RequestBody DriverDto driverDto) {
        if (!isValidStatus(driverDto.getCurrentStatus())) {
            return ResponseEntity.badRequest().build();
        }

        DriverDto createdDriver = driverService.createDriver(driverDto);

        // Отправляем событие в Kafka
        kafkaTemplate.send("driver-events", Map.of(
                "eventType", "CREATED",
                "driverId", createdDriver.getId().toString(),
                "name", createdDriver.getName(),
                "ordersCompleted", createdDriver.getOrdersCompleted(),
                "currentStatus", createdDriver.getCurrentStatus()
        ));

        return ResponseEntity.ok(createdDriver);
    }

    private boolean isValidStatus(String status) {
        return status != null && (status.equals("AVAILABLE") || status.equals("BUSY") || status.equals("OFFLINE"));
    }

    @GetMapping
    public ResponseEntity<List<DriverDto>> getDrivers() {
        List<DriverDto> driverDtoList = driverService.getAllDrivers();
        if (driverDtoList.isEmpty()) {
            LOG.info("No drivers found");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(driverDtoList);
    }
}
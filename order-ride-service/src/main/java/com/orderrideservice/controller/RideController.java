package com.orderrideservice.controller;

import com.orderrideservice.dto.RideDto;
import com.orderrideservice.exception.RideNotFoundException; // Создай этот класс исключения
import com.orderrideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private static final Logger LOG = LoggerFactory.getLogger(RideController.class);

    @PostMapping
    public ResponseEntity<RideDto> createRide(@RequestBody RideDto rideDto) {
        LOG.info("Creating ride: {}", rideDto);
        RideDto createdRide = rideService.createRide(rideDto);
        LOG.info("Ride created: {}", createdRide);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRide);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDto> getRideById(@PathVariable UUID id) {
        LOG.info("Getting ride by id: {}", id);
        return rideService.getRideById(id)
                .map(rideDto -> {
                    LOG.info("Ride found: {}", rideDto);
                    return ResponseEntity.ok(rideDto);
                })
                .orElseGet(() -> {
                    LOG.warn("Ride not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    public ResponseEntity<List<RideDto>> getRides() {
        LOG.info("Getting all rides");
        List<RideDto> rideDtoList = rideService.getAllRides();
        if (rideDtoList.isEmpty()) {
            LOG.info("No rides found");
            return ResponseEntity.noContent().build();
        } else {
            LOG.info("Found {} rides", rideDtoList.size());
            return ResponseEntity.ok().body(rideDtoList);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RideDto> updateRide(@PathVariable UUID id, @RequestBody RideDto rideDto) {
        LOG.info("Updating ride with id: {}, data: {}", id, rideDto);
        try {
            RideDto updatedRide = rideService.updateRide(id, rideDto);
            LOG.info("Ride updated: {}", updatedRide);
            return ResponseEntity.ok(updatedRide);
        } catch (RideNotFoundException e) {
            LOG.warn("Cannot update ride, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error updating ride with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable UUID id) {
        LOG.info("Deleting ride with id: {}", id);
        try {
            rideService.deleteRide(id);
            LOG.info("Ride deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RideNotFoundException e) {
            LOG.warn("Cannot delete ride, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error deleting ride with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
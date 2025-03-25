package com.orderrideservice.service;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.DriverCache;
import com.orderrideservice.entity.Ride;
import com.orderrideservice.exception.DriverNotFoundException;
import com.orderrideservice.mapper.RideMapper;
import com.orderrideservice.repository.DriverCacheRepository;
import com.orderrideservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final DriverCacheRepository driverCacheRepository;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @Transactional
    public RideDto createRide(RideDto rideDto) {
        DriverCache driver = driverCacheRepository.findById(rideDto.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(rideDto.getDriverId()));

        if (!driver.isActive()) {
            throw new IllegalStateException("Driver is not active");
        }

        Ride ride = rideMapper.toEntity(rideDto);
        Ride savedRide = rideRepository.save(ride);

        kafkaTemplate.send("ride-events", Map.of(
                "eventType", "CREATED",
                "rideId", savedRide.getId().toString(),
                "driverId", savedRide.getDriverId().toString(),
                "orderId", savedRide.getOrderId().toString()
        ));

        return rideMapper.toDto(savedRide);
    }

    public List<RideDto> getAllRides() {
        return rideRepository.findAll().stream()
                .map(rideMapper::toDto)
                .toList();
    }
}

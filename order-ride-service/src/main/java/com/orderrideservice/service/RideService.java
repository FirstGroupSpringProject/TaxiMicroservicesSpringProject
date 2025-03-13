package com.orderrideservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.Ride;
import com.orderrideservice.mapper.RideMapper;
import com.orderrideservice.repository.RideRepository;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private static Logger log = LoggerFactory.getLogger(RideService.class);

    @Autowired
    public RideService(RideRepository rideRepository, RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
    }

    public RideDto createRide(RideDto rideDto) {
        Ride ride = rideMapper.toEntity(rideDto);
        Ride savedRide = rideRepository.save(ride);
        return rideMapper.toDto(savedRide);
    }

    public List<RideDto> getAllRides() {
        List<Ride> riders = rideRepository.findAll();
        if (riders.isEmpty()) {
            log.info("No riders found");
            return null;
        } else {
            return riders.stream()
                    .map(rideMapper::toDto)
                    .toList();
        }
    }
}

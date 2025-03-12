package com.userdriverservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.userdriverservice.dto.VehicleDto;
import com.userdriverservice.entity.Vehicle;
import com.userdriverservice.mapper.VehicleMapper;
import com.userdriverservice.repository.VehicleRepository;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(savedVehicle);
    }

    public VehicleDto getVehicleByNumber(String number) {
        Vehicle vehicle = vehicleRepository.findVehicleByNumber(number);
        return vehicleMapper.toDto(vehicle);
    }

    public VehicleDto updateVehicle(UUID id, VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        vehicleMapper.updateVehicle(vehicleDto, vehicle);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(updatedVehicle);
    }

    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            LOG.info("No vehicles found");
            return null;
        } else {
            return vehicles.stream()
                    .map(vehicleMapper::toDto)
                    .toList();
        }
    }
}

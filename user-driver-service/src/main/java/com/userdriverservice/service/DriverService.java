package com.userdriverservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.entity.Driver;
import com.userdriverservice.mapper.DriverMapper;
import com.userdriverservice.repository.DriverRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private static Logger log = LoggerFactory.getLogger(DriverService.class);

    @Autowired
    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    public DriverDto createDriver(DriverDto driverDto) {
        Driver driver = driverMapper.toEntity(driverDto);
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toDto(savedDriver);
    }

    public DriverDto updateDriver(UUID id, DriverDto driverDto) {
        Driver driver = driverRepository.findById(id).orElseThrow();
        driverMapper.updateDriver(driverDto, driver);
        Driver updatedDriver = driverRepository.save(driver);
        return driverMapper.toDto(updatedDriver);
    }

    public List<DriverDto> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        if (drivers.isEmpty()) {
            log.info("No drivers found");
            return null;
        } else {
            return drivers.stream()
                    .map(driverMapper::toDto)
                    .toList();
        }
    }
}

package com.userdriverservice.service;
import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.dto.UserDto;
import com.userdriverservice.entity.Driver;
import com.aston.commonevents.dto.DriverEvent;
import com.aston.commonevents.dto.DriverEventPayload;
import com.userdriverservice.entity.User;
import com.userdriverservice.exception.DriverNotFoundException;
import com.userdriverservice.mapper.DriverMapper;
import com.userdriverservice.mapper.UserMapper;
import com.userdriverservice.repository.DriverRepository;
import com.userdriverservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DriverMapper driverMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String DRIVER_EVENTS_TOPIC = "driver-events";

    @Transactional
    public DriverDto createDriver(DriverDto driverDto) {
        log.info("Attempting to create driver: {}", driverDto.getName());
        if (!isValidStatus(driverDto.getCurrentStatus())) {
            log.warn("Invalid status provided: {}", driverDto.getCurrentStatus());
            throw new IllegalArgumentException("Invalid driver status: " + driverDto.getCurrentStatus());
        }
        Driver driver = driverMapper.toEntity(driverDto);
        if (driver.getOrdersCompleted() == null) {
            driver.setOrdersCompleted(0);
        }
        Driver savedDriver = driverRepository.save(driver);
        log.info("Driver saved with id: {}", savedDriver.getId());
        DriverDto savedDto = driverMapper.toDto(savedDriver);

        sendDriverEvent(savedDto, "CREATED");
        return savedDto;
    }

    @Transactional(readOnly = true)
    public Optional<DriverDto> getDriverById(UUID id) {
        log.debug("Finding driver by id: {}", id);
        return driverRepository.findById(id).map(driverMapper::toDto);
    }


    @Transactional
    public DriverDto updateDriver(UUID id, DriverDto driverDto) {
        log.info("Attempting to update driver with id: {}", id);
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        if (driverDto.getCurrentStatus() != null && !isValidStatus(driverDto.getCurrentStatus())) {
            log.warn("Invalid status provided for update: {}", driverDto.getCurrentStatus());
            throw new IllegalArgumentException("Invalid driver status: " + driverDto.getCurrentStatus());
        }

        driverMapper.updateDriver(driverDto, existingDriver);
        Driver updatedDriver = driverRepository.save(existingDriver);
        log.info("Driver updated successfully: {}", updatedDriver.getId());
        DriverDto updatedDto = driverMapper.toDto(updatedDriver);

        sendDriverEvent(updatedDto, "UPDATED");
        return updatedDto;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.debug("Finding all users");
        List<User> users = userRepository.findAll();
        // Не возвращаем null, возвращаем пустой список
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID id) {
        log.debug("Finding user by id: {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Transactional
    public void deleteDriver(UUID id) {
        log.info("Attempting to delete driver with id: {}", id);
        Driver driverToDelete = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        // Сохраняем DTO перед удалением для отправки события
        DriverDto deletedDto = driverMapper.toDto(driverToDelete);

        driverRepository.deleteById(id);
        log.info("Driver deleted successfully with id: {}", id);

        sendDriverEvent(deletedDto, "DELETED");
    }


    @Transactional(readOnly = true)
    public List<DriverDto> getAllDrivers() {
        log.debug("Finding all drivers");
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream()
                .map(driverMapper::toDto)
                .collect(Collectors.toList());
    }

    // Вспомогательный метод для отправки событий
    private void sendDriverEvent(DriverDto driverDto, String eventType) {
        try {
            DriverEventPayload payload = new DriverEventPayload(
                    driverDto.getId(),
                    driverDto.getName(),
                    driverDto.getOrdersCompleted(),
                    driverDto.getCurrentStatus(),
                    !"DELETED".equals(eventType) // Поле active: true для CREATED/UPDATED, false для DELETED
            );
            DriverEvent event = new DriverEvent(eventType, payload);

            log.info("Sending driver event to Kafka. Type: {}, Driver ID: {}", eventType, driverDto.getId());
            kafkaTemplate.send(DRIVER_EVENTS_TOPIC, driverDto.getId().toString(), event);
        } catch (Exception e) {
            log.error("Failed to send driver event to Kafka for driver id: {}. Type: {}", driverDto.getId(), eventType, e);
        }
    }

    private boolean isValidStatus(String status) {

        return status != null && ("AVAILABLE".equals(status) || "BUSY".equals(status) || "OFFLINE".equals(status));
    }
}
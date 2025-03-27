package com.userdriverservice.service;

import com.userdriverservice.dto.VehicleDto;
import com.userdriverservice.entity.Vehicle;
import com.userdriverservice.exception.VehicleNotFoundException;
import com.userdriverservice.mapper.VehicleMapper;
import com.userdriverservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с транспортными средствами.
 * Предоставляет CRUD-операции и бизнес-логику для управления транспортными средствами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String VEHICLE_EVENTS_TOPIC = "vehicle-events";

    /**
     * Создает новое транспортное средство.
     *
     * @param vehicleDto DTO с данными транспортного средства
     * @return созданный VehicleDto
     */
    @Transactional
    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        log.info("Creating vehicle with number: {}", vehicleDto.getNumber());
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle created with ID: {}", savedVehicle.getId());
        VehicleDto savedDto = vehicleMapper.toDto(savedVehicle);

        sendVehicleEvent(savedDto, "CREATED");
        return savedDto;
    }
    /**
     * Получает транспортное средство по идентификатору.
     *
     * @param id UUID транспортного средства
     * @return Optional с VehicleDto, если транспортное средство найдено
     */
    @Transactional(readOnly = true)
    public Optional<VehicleDto> getVehicleById(UUID id) {
        log.debug("Fetching vehicle by ID: {}", id);
        return vehicleRepository.findById(id).map(vehicleMapper::toDto);
    }
    /**
     * Обновляет данные транспортного средства.
     *
     * @param id UUID транспортного средства
     * @param vehicleDto DTO с обновленными данными
     * @return обновленный VehicleDto
     * @throws VehicleNotFoundException если транспортное средство не найдено
     */
    @Transactional
    public VehicleDto updateVehicle(UUID id, VehicleDto vehicleDto) {
        log.info("Updating vehicle with ID: {}", id);
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        vehicleMapper.updateVehicle(vehicleDto, existingVehicle);
        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        log.info("Vehicle updated: {}", updatedVehicle.getId());
        VehicleDto updatedDto = vehicleMapper.toDto(updatedVehicle);

        sendVehicleEvent(updatedDto, "UPDATED");
        return updatedDto;
    }
    /**
     * Удаляет транспортное средство.
     *
     * @param id UUID транспортного средства
     * @throws VehicleNotFoundException если транспортное средство не найдено
     */
    @Transactional
    public void deleteVehicle(UUID id) {
        log.info("Deleting vehicle with ID: {}", id);
        Vehicle vehicleToDelete = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        vehicleRepository.deleteById(id);
        log.info("Vehicle deleted: {}", id);

        sendVehicleEvent(vehicleMapper.toDto(vehicleToDelete), "DELETED");
    }
    /**
     * Получает список всех транспортных средств.
     *
     * @return список VehicleDto
     */
    @Transactional(readOnly = true)
    public List<VehicleDto> getAllVehicles() {
        log.debug("Fetching all vehicles");
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }
    /**
     * Отправляет событие о транспортном средстве в Kafka.
     *
     * @param vehicleDto DTO транспортного средства
     * @param eventType тип события ("CREATED", "UPDATED", "DELETED")
     */
    private void sendVehicleEvent(VehicleDto vehicleDto, String eventType) {
        try {
            Map<String, Object> eventData = Map.of(
                    "eventType", eventType,
                    "vehicleId", vehicleDto.getId().toString(),
                    "driverId", vehicleDto.getDriverId().toString(),
                    "model", vehicleDto.getModel(),
                    "number", vehicleDto.getNumber()
            );
            log.info("Sending vehicle event to Kafka: {}", eventData);
            kafkaTemplate.send(VEHICLE_EVENTS_TOPIC, vehicleDto.getId().toString(), eventData);
        } catch (Exception e) {
            log.error("Failed to send vehicle event for ID: {}", vehicleDto.getId(), e);
        }
    }
}
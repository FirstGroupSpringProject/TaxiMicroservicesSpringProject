package com.userdriverservice.controller;

import com.userdriverservice.dto.VehicleDto;
import com.userdriverservice.exception.VehicleNotFoundException;
import com.userdriverservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
/**
 * Контроллер для управления транспортными средствами.
 * Предоставляет REST API для операций CRUD с транспортными средствами.
 */
@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;
    private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);

    /**
     * Создает новое транспортное средство.
     *
     * @param vehicleDto DTO с данными транспортного средства
     * @return ResponseEntity с созданным VehicleDto и статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@RequestBody VehicleDto vehicleDto) {
        LOG.info("Creating vehicle: {}", vehicleDto);
        VehicleDto createdVehicle = vehicleService.createVehicle(vehicleDto);
        LOG.info("Vehicle created: {}", createdVehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
    }
    /**
     * Получает транспортное средство по идентификатору.
     *
     * @param id UUID транспортного средства
     * @return ResponseEntity с VehicleDto и статусом 200 (OK), если найдено,
     *         или статусом 404 (Not Found), если не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable UUID id) {
        LOG.info("Getting vehicle by id: {}", id);
        return vehicleService.getVehicleById(id)
                .map(vehicleDto -> {
                    LOG.info("Vehicle found: {}", vehicleDto);
                    return ResponseEntity.ok(vehicleDto);
                })
                .orElseGet(() -> {
                    LOG.warn("Vehicle not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
    /**
     * Получает список всех транспортных средств.
     *
     * @return ResponseEntity со списком VehicleDto и статусом 200 (OK),
     *         или статусом 204 (No Content), если транспортные средства не найдены
     */
    @GetMapping
    public ResponseEntity<List<VehicleDto>> getVehicles() {
        LOG.info("Getting all vehicles");
        List<VehicleDto> vehicleDtoList = vehicleService.getAllVehicles();
        if (vehicleDtoList.isEmpty()) {
            LOG.info("No vehicles found");
            return ResponseEntity.noContent().build();
        }
        LOG.info("Found {} vehicles", vehicleDtoList.size());
        return ResponseEntity.ok(vehicleDtoList);
    }
    /**
     * Обновляет данные транспортного средства.
     *
     * @param id UUID транспортного средства
     * @param vehicleDto DTO с обновленными данными
     * @return ResponseEntity с обновленным VehicleDto и статусом 200 (OK),
     *         или статусом 404 (Not Found), если транспортное средство не найдено,
     *         или статусом 500 (Internal Server Error) при ошибке
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable UUID id, @RequestBody VehicleDto vehicleDto) {
        LOG.info("Updating vehicle with id: {}, data: {}", id, vehicleDto);
        try {
            VehicleDto updatedVehicle = vehicleService.updateVehicle(id, vehicleDto);
            LOG.info("Vehicle updated: {}", updatedVehicle);
            return ResponseEntity.ok(updatedVehicle);
        } catch (VehicleNotFoundException e) {
            LOG.warn("Cannot update vehicle, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error updating vehicle with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * Удаляет транспортное средство.
     *
     * @param id UUID транспортного средства
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении,
     *         или статусом 404 (Not Found), если транспортное средство не найдено,
     *         или статусом 500 (Internal Server Error) при ошибке
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) {
        LOG.info("Deleting vehicle with id: {}", id);
        try {
            vehicleService.deleteVehicle(id);
            LOG.info("Vehicle deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (VehicleNotFoundException e) {
            LOG.warn("Cannot delete vehicle, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error deleting vehicle with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

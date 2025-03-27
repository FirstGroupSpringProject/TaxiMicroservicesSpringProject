package com.userdriverservice.controller;
import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.exception.DriverNotFoundException;
import com.userdriverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления водителями.
 * Предоставляет REST API для операций CRUD с водителями.
 */
@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);

    /**
     * Создает нового водителя.
     *
     * @param driverDto DTO с данными водителя
     * @return ResponseEntity с созданным DriverDto и статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<DriverDto> createDriver(@RequestBody DriverDto driverDto) {
        LOG.info("Creating driver: {}", driverDto);
        DriverDto createdDriver = driverService.createDriver(driverDto);
        LOG.info("Driver created: {}", createdDriver);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDriver);
    }
    /**
     * Получает водителя по идентификатору.
     *
     * @param id UUID водителя
     * @return ResponseEntity с DriverDto и статусом 200 (OK), если найден,
     *         или статусом 404 (Not Found), если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getDriverById(@PathVariable UUID id) {
        LOG.info("Getting driver by id: {}", id);
        return driverService.getDriverById(id)
                .map(driverDto -> {
                    LOG.info("Driver found: {}", driverDto);
                    return ResponseEntity.ok(driverDto);
                })
                .orElseGet(() -> {
                    LOG.warn("Driver not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Получает список всех водителей.
     *
     * @return ResponseEntity со списком DriverDto и статусом 200 (OK),
     *         или статусом 204 (No Content), если водители не найдены
     */
    @GetMapping
    public ResponseEntity<List<DriverDto>> getDrivers() {
        LOG.info("Getting all drivers");
        List<DriverDto> driverDtoList = driverService.getAllDrivers();
        if (driverDtoList.isEmpty()) {
            LOG.info("No drivers found");
            return ResponseEntity.noContent().build();
        }
        LOG.info("Found {} drivers", driverDtoList.size());
        return ResponseEntity.ok(driverDtoList);
    }

    /**
     * Обновляет данные водителя.
     *
     * @param id UUID водителя
     * @param driverDto DTO с обновленными данными
     * @return ResponseEntity с обновленным DriverDto и статусом 200 (OK),
     *         или статусом 404 (Not Found), если водитель не найден,
     *         или статусом 500 (Internal Server Error) при ошибке
     */
    @PutMapping("/{id}")
    public ResponseEntity<DriverDto> updateDriver(@PathVariable UUID id, @RequestBody DriverDto driverDto) {
        LOG.info("Updating driver with id: {}, data: {}", id, driverDto);
        try {
            DriverDto updatedDriver = driverService.updateDriver(id, driverDto);
            LOG.info("Driver updated: {}", updatedDriver);
            return ResponseEntity.ok(updatedDriver);
        } catch (DriverNotFoundException e) {
            LOG.warn("Cannot update driver, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error updating driver with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Удаляет водителя.
     *
     * @param id UUID водителя
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении,
     *         или статусом 404 (Not Found), если водитель не найден,
     *         или статусом 500 (Internal Server Error) при ошибке
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable UUID id) {
        LOG.info("Deleting driver with id: {}", id);
        try {
            driverService.deleteDriver(id);
            LOG.info("Driver deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (DriverNotFoundException e) {
            LOG.warn("Cannot delete driver, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error deleting driver with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
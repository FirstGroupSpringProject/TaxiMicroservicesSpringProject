package com.orderrideservice.exception;

import java.util.UUID;

/**
 * Исключение, выбрасываемое когда водитель не найден в системе.
 * Автоматически возвращает HTTP-статус 404 (Not Found).
 */
public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(UUID driverId) {
        super("Driver not found with id: " + driverId);
    }
}

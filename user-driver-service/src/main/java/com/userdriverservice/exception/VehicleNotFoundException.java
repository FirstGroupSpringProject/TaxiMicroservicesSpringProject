package com.userdriverservice.exception;

import java.util.UUID;
/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему транспортному средству.
 */
public class VehicleNotFoundException extends RuntimeException {
    /**
     * Создает исключение с сообщением о ненайденном транспортном средстве.
     * @param id идентификатор транспортного средства, который не был найден
     */
    public VehicleNotFoundException(UUID id) {
        super("Vehicle not found with id: " + id);
    }
}
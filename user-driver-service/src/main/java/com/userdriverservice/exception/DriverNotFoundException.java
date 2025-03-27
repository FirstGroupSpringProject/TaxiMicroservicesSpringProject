package com.userdriverservice.exception;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда запрашиваемая сущность не найдена.
 * Это исключение указывает на то, что сервер не может найти запрашиваемую
 * сущность по предоставленному идентификатору. Оно возвращает статус HTTP 404 (NOT FOUND).
 */
public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(UUID id) {
        super("Driver not found with id: " + id);
    }
}


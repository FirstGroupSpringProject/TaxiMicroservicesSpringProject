package com.userdriverservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда запрашиваемая сущность не найдена.
 * Это исключение указывает на то, что сервер не может найти запрашиваемую
 * сущность по предоставленному идентификатору. Оно возвращает статус HTTP 404 (NOT FOUND).
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Driver with id " + id + " not found");
    }
}
package com.userdriverservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда запрашиваемая сущность не найдена.
 * Это исключение указывает на то, что сервер не может найти запрашиваемую
 * сущность по предоставленному идентификатору. Оно возвращает статус HTTP 404 (NOT FOUND).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Конструктор исключения, принимающий идентификатор сущности.
     *
     * @param id идентификатор сущности, которая не найдена.
     */
    public NotFoundException(UUID id) {
        super("Entity is not found #" + id);
    }
}
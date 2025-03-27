package com.userdriverservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается в случае неверного запроса.
 * Это исключение указывает на то, что клиентский запрос был некорректным
 * и не может быть обработан сервером. Оно возвращает статус HTTP 400 (BAD REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Конструктор исключения, принимающий идентификатор сущности.
     *
     * @param id идентификатор сущности, которая не найдена или была передана неверно.
     */
    public BadRequestException(UUID id) {
        super("Entity is not found #" + id);
    }
}
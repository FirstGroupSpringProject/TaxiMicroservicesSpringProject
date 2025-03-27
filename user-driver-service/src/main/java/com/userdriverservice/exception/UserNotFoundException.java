package com.userdriverservice.exception;

import java.util.UUID;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему пользователю.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Создает исключение с сообщением о ненайденном пользователе.
     * @param id идентификатор пользователя, который не был найден
     */
    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }
    /**
     * Создает исключение с сообщением о ненайденном пользователе.
     * @param id идентификатор пользователя в строковом формате
     * @param message дополнительное сообщение об ошибке
     */
    public UserNotFoundException(String id, String message) {
        super("User not found with id: " + id + ". " + message);
    }
}
package com.orderrideservice.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

/**
 * Исключение, выбрасываемое когда поездка не найдена в системе.
 * Автоматически возвращает HTTP-статус 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RideNotFoundException extends RuntimeException {
  public RideNotFoundException(UUID id) {
    super("Ride not found with id: " + id);
  }
}
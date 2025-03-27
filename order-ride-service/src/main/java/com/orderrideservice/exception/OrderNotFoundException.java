package com.orderrideservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

/**
 * Исключение, выбрасываемое когда заказ не найден в системе.
 * Автоматически возвращает HTTP-статус 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(UUID id) {
    super("Order not found with id: " + id);
  }
}

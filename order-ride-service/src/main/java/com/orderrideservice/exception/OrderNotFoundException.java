package com.orderrideservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND) // Автоматически вернет 404
public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(UUID id) {
    super("Order not found with id: " + id);
  }
}

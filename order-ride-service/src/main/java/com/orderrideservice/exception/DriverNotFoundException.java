package com.orderrideservice.exception;

import java.util.UUID;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(UUID driverId) {
        super("Driver not found with id: " + driverId);
    }
}

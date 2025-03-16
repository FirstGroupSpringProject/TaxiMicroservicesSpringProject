package com.aston.paymentbillingservice.entity;

/**
 * Перечисление, представляющее возможные статусы платежей.
 * <p>
 * Это перечисление содержит три статуса, которые могут быть присвоены
 * платежам в системе: в ожидании (PENDING), успешно завершен (SUCCEEDED)
 * и неудача (FAILED).
 */
public enum PaymentStatus {

    PENDING, // В ожидании
    SUCCEEDED, // Успешно завершен
    FAILED // Неудача
}

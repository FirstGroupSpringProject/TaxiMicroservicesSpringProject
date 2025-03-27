package com.aston.paymentbillingservice.entity;

/**
 * Перечисление, представляющее доступные методы оплаты.
 * Это перечисление содержит три метода оплаты, которые могут быть использованы
 * в системе: карта (CARD), наличные (CASH) и мобильный платеж (MOBILE).
 */
public enum PaymentMethod {

    CARD,
    CASH,
    MOBILE
}

package com.aston.paymentbillingservice.dto;

import com.aston.paymentbillingservice.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) для платежа.
 * Этот класс используется для передачи информации о платеже между слоями приложения,
 * такими как слой представления и слой сервиса.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDto {

    private UUID id; // Идентификатор
    private UUID orderId; // Идентификатор заказа
    private UUID userId; // Идентификатор пользователя
    private BigDecimal amount; // Сумма платежа
    private PaymentStatus status; // Статус платежа

    // equals и hashCode нужно обновить, если используешь их
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDto that = (PaymentDto) o;
        return Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId) && Objects.equals(userId, that.userId) && Objects.equals(amount, that.amount) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, userId, amount, status);
    }
}
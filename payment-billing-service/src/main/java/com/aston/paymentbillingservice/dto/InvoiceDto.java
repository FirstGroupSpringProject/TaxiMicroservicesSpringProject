package com.aston.paymentbillingservice.dto;

import com.aston.paymentbillingservice.entity.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) для счета-фактуры.
 * Этот класс используется для передачи информации о счете между слоями приложения,
 * такими как слой представления и слой сервиса.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDto {

    private UUID id;
    private UUID userId;
    private PaymentMethod paymentMethod;
    private LocalDateTime dueDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDto that = (InvoiceDto) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId)
                && paymentMethod == that.paymentMethod && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, paymentMethod, dueDate);
    }
}

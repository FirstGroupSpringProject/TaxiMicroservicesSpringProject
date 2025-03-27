package com.aston.paymentbillingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, представляющий сущность счета.
 * Этот класс содержит информацию о счете, включая идентификатор пользователя,
 * метод оплаты и срок оплаты.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;
}

package com.aston.paymentbillingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Класс, представляющий сущность платежа.
 * Этот класс содержит информацию о платеже, включая сумму и статус.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // Сумма

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status; // Статус
}

package com.aston.paymentbillingservice.repository;

import com.aston.paymentbillingservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Payment.
 * Этот интерфейс наследует функциональность от JpaRepository,
 * что позволяет выполнять стандартные операции CRUD (создание, чтение, обновление, удаление)
 * с объектами Payment в базе данных.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}

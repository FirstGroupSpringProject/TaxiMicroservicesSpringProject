package com.aston.paymentbillingservice.repository;

import com.aston.paymentbillingservice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Invoice.
 * Этот интерфейс наследует функциональность от JpaRepository,
 * что позволяет выполнять стандартные операции CRUD (создание, чтение, обновление, удаление)
 * с объектами Invoice в базе данных.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}
package com.orderrideservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

/**
 * Сущность для кэширования информации о водителе.
 * Хранит часто запрашиваемые данные для быстрого доступа.
 */
@Entity
@Table(name = "driver_cache")
@Data
public class DriverCache {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "orders_completed")
    private Integer ordersCompleted;

    @Column(name = "current_status")
    private String currentStatus;

    @Column(nullable = false)
    private boolean active = true;
}
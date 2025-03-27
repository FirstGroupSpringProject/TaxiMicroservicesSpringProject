package com.userdriverservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сущность водителя.
 * Представляет данные водителя в системе и отображается на таблицу "drivers" в базе данных.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "orders_completed", nullable = false)
    private Integer ordersCompleted;

    @Column(name = "current_status", nullable = false)
    private String currentStatus;
}
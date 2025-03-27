package com.userdriverservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * Сущность транспортного средства.
 * Представляет данные транспортного средства в системе и отображается на таблицу "vehicles" в базе данных.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

}

package com.userdriverservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "driver_id", nullable = false) // связь с Driver
    private UUID driverId;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

}

package com.orderrideservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.orderrideservice.entity.Ride;

import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {
}

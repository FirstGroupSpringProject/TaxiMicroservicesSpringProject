package com.orderrideservice.repository;

import com.orderrideservice.entity.DriverCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverCacheRepository extends JpaRepository<DriverCache, UUID> {
    boolean existsById(UUID driverId);
}
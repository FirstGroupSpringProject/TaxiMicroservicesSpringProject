package com.orderrideservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.orderrideservice.entity.Route;

import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {
}

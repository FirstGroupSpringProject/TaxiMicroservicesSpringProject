package com.orderrideservice.repository;

import com.orderrideservice.entity.UserRideHistory;
import com.orderrideservice.entity.UserRideHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRideHistoryRepository extends JpaRepository<UserRideHistory, UserRideHistoryId> {

    List<UserRideHistory> findById_UserId(UUID userId);

    List<UserRideHistory> findById_RideId(UUID rideId);

    boolean existsById_UserIdAndId_RideId(UUID userId, UUID rideId);

    Optional<UserRideHistory> findById_UserIdAndId_RideId(UUID userId, UUID rideId);
}
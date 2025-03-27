package com.userdriverservice.kafka.consumer;


import com.userdriverservice.entity.Driver;
import com.aston.commonevents.dto.RideCompletedEvent;
import com.userdriverservice.exception.DriverNotFoundException;
import com.userdriverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Kafka Consumer для обработки событий завершения поездок.
 * Обновляет статистику водителей при завершении поездок.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RideEventConsumer {
    private final DriverRepository driverRepository;

    /**
     * Обрабатывает событие завершения поездки.
     * Увеличивает счетчик завершенных заказов у водителя.
     *
     * @param event событие завершения поездки
     */
    @KafkaListener(topics = "${spring.kafka.topics.ride-events:ride-events}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handleRideCompleted(RideCompletedEvent event) {
        log.info("Received RideCompletedEvent for rideId={}, driverId={}", event.getRideId(), event.getDriverId());
        if (event.getDriverId() == null) {
            log.warn("Received RideCompletedEvent with null driverId for rideId={}", event.getRideId());
            return;
        }
        try {
            Driver driver = driverRepository.findById(event.getDriverId())
                    .orElseThrow(() -> new DriverNotFoundException(event.getDriverId()));

            int currentCompleted = driver.getOrdersCompleted() != null ? driver.getOrdersCompleted() : 0;
            driver.setOrdersCompleted(currentCompleted + 1);
            driverRepository.save(driver);
            log.info("Incremented ordersCompleted for driver {}. New count: {}", driver.getId(), driver.getOrdersCompleted());

        } catch (DriverNotFoundException e) {
            log.warn("Driver {} not found while processing RideCompletedEvent for rideId={}. Skipping.", event.getDriverId(), event.getRideId());
        } catch (Exception e) {
            log.error("Error processing RideCompletedEvent for rideId={}, driverId={}. Error: {}",
                    event.getRideId(), event.getDriverId(), e.getMessage(), e);
            throw new RuntimeException("Failed to process RideCompletedEvent: " + e.getMessage(), e);
        }
    }
}
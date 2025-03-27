package com.orderrideservice.kafka.consumer;

import com.orderrideservice.entity.DriverCache;
import com.orderrideservice.repository.DriverCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverEventConsumer {
    private final DriverCacheRepository driverCacheRepository;

    @KafkaListener(topics = "driver-events")
    public void handleDriverEvent(Map<String, Object> event) {
        try {
            String eventType = (String) event.get("eventType");
            UUID driverId = UUID.fromString((String) event.get("driverId"));

            switch (eventType) {
                case "CREATED":
                case "UPDATED":
                    DriverCache cache = new DriverCache();
                    cache.setId(driverId);
                    cache.setName((String) event.get("name"));
                    cache.setOrdersCompleted((Integer) event.get("ordersCompleted"));
                    cache.setCurrentStatus((String) event.get("currentStatus"));
                    cache.setActive(true);
                    driverCacheRepository.save(cache);
                    break;

                case "DELETED":
                    driverCacheRepository.deleteById(driverId);
                    break;

                default:
                    log.warn("Unknown event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing driver event: {}", event, e);
        }
    }
}
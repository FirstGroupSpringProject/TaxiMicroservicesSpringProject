package com.userdriverservice.kafka.consumer; // Создай этот пакет


import com.userdriverservice.entity.Driver;
import com.aston.commonevents.dto.RideCompletedEvent; // ИЗМЕНЕНО: Импорт из общего модуля
import com.userdriverservice.exception.DriverNotFoundException;
import com.userdriverservice.repository.DriverRepository;
// import com.userdriverservice.service.DriverService; // Не нужен для отправки, т.к. DriverService сам отправит при save
// import com.userdriverservice.mapper.DriverMapper; // Не нужен
import com.userdriverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideEventConsumer {

    private final DriverRepository driverRepository;
    private final DriverService driverService; // Для отправки DriverEvent после обновления

    // Убедись, что топик и group-id совпадают с настройками в application.yml
    // Используй правильный тип данных события (RideCompletedEvent)
    @KafkaListener(topics = "${spring.kafka.topics.ride-events:ride-events}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional // Важно для обновления счетчика
    public void handleRideCompleted(RideCompletedEvent event) {
        log.info("Received RideCompletedEvent for rideId: {}, driverId: {}", event.getRideId(), event.getDriverId());
        try {
            Driver driver = driverRepository.findById(event.getDriverId())
                    .orElseThrow(() -> new DriverNotFoundException(event.getDriverId()));

            int currentCompleted = driver.getOrdersCompleted() != null ? driver.getOrdersCompleted() : 0;
            driver.setOrdersCompleted(currentCompleted + 1);
            Driver updatedDriver = driverRepository.save(driver);
            log.info("Incremented ordersCompleted for driver {}. New count: {}", updatedDriver.getId(), updatedDriver.getOrdersCompleted());

            // Опционально: Отправить событие об обновлении водителя в driver-events,
            // чтобы другие сервисы (например, ORS) обновили свой кэш
            // driverService.sendDriverEvent(driverMapper.toDto(updatedDriver), "UPDATED"); // Нужен mapper

        } catch (DriverNotFoundException e) {
            log.warn("Driver not found while processing RideCompletedEvent: {}", event.getDriverId());
            // TODO: Решить, что делать (пропустить, записать в dead-letter queue)
        } catch (Exception e) {
            log.error("Error processing RideCompletedEvent for rideId: {}", event.getRideId(), e);
            // TODO: Стратегия обработки ошибок
        }
    }
}
package com.orderrideservice.service;
import ch.qos.logback.classic.Logger;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.*; // Импорт всех сущностей пакета
import com.orderrideservice.exception.DriverNotFoundException;
import com.orderrideservice.exception.OrderNotFoundException;
import com.orderrideservice.exception.RideNotFoundException;
import com.orderrideservice.mapper.RideMapper;
import com.orderrideservice.repository.DriverCacheRepository;
import com.orderrideservice.repository.OrderRepository;
import com.orderrideservice.repository.RideRepository;
import com.aston.commonevents.dto.RideCompletedEvent; // Импорт из общего модуля
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.orderrideservice.repository.UserRideHistoryRepository; // Добавить импорт
import com.orderrideservice.entity.UserRideHistory; // Добавить импорт

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final OrderRepository orderRepository;
    private final DriverCacheRepository driverCacheRepository;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private KafkaTemplate<String, RideCompletedEvent> rideKafkaTemplate; // Типизированный KafkaTemplate
    private static final String RIDE_EVENTS_TOPIC = "ride-events";
    private static final String PAYMENT_REQUEST_TOPIC = "payment-requests";
    private final UserRideHistoryRepository userRideHistoryRepository;

    @Transactional
    public RideDto createRide(RideDto rideDto) {
        DriverCache driver = driverCacheRepository.findById(rideDto.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(rideDto.getDriverId()));

        if (!driver.isActive()) {
            throw new IllegalStateException("Driver is not active");
        }

        Ride ride = rideMapper.toEntity(rideDto);
        Ride savedRide = rideRepository.save(ride);

        kafkaTemplate.send("ride-events", Map.of(
                "eventType", "CREATED",
                "rideId", savedRide.getId().toString(),
                "driverId", savedRide.getDriverId().toString(),
                "orderId", savedRide.getOrderId().toString()
        ));

        return rideMapper.toDto(savedRide);


    }
    private void createUserRideHistoryEntry(UUID userId, UUID rideId) {
        // Проверяем, нет ли уже такой записи (используем existsById)
        UserRideHistoryId historyId = new UserRideHistoryId(userId, rideId);
        Logger log = null;
        if (!userRideHistoryRepository.existsById(historyId)) {
            UserRideHistory historyEntry = new UserRideHistory(userId, rideId);
            // createdAt установится автоматически
            userRideHistoryRepository.save(historyEntry);
            log.info("Created UserRideHistory entry for user {} and ride {}", userId, rideId);
        } else {
            log.warn("UserRideHistory entry for user {} and ride {} already exists.", userId, rideId);
        }
    }


    @Transactional
    public RideDto completeRide(UUID rideId) { // Пример метода завершения
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
        Order order = orderRepository.findById(ride.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ride.getOrderId()));

        // Меняем статус заказа (если еще не COMPLETED)
        if (order.getStatus() != OrderStatus.COMPLETED) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            Logger log = null;
            log.info("Order {} status set to COMPLETED", order.getId());

            // --- Отправка события о завершении поездки ---
            // TODO: Рассчитать сумму (amount) поездки
            BigDecimal rideAmount = calculateRideAmount(ride.getDistance()); // Нужен метод расчета

            RideCompletedEvent event = new RideCompletedEvent(
                    ride.getId(),
                    ride.getOrderId(),
                    ride.getDriverId(),
                    order.getUserId(), // Берем userId из заказа
                    rideAmount
            );
            try {
                log.info("Sending RideCompletedEvent for ride id: {}", rideId);
                rideKafkaTemplate.send(RIDE_EVENTS_TOPIC, ride.getId().toString(), event);
            } catch (Exception e) {
                log.error("Failed to send RideCompletedEvent for ride id: {}", rideId, e);
                // TODO: Обработка ошибок отправки
            }
        }
        // Возвращаем обновленный RideDto или что-то другое
        return rideMapper.toDto(ride); // dto может не содержать статус, т.к. статус у Order
    }


    private BigDecimal calculateRideAmount(Double distance) {
        if (distance == null || distance <= 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(distance).multiply(BigDecimal.valueOf(15.5)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Transactional(readOnly = true)
    public Optional<RideDto> getRideById(UUID id) {
        Logger log = null;
        log.debug("Finding ride by id: {}", id);
        return rideRepository.findById(id).map(rideMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RideDto> getAllRides() {
        Logger log = null;
        log.debug("Finding all rides");
        return rideRepository.findAll().stream()
                .map(rideMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RideDto updateRide(UUID id, RideDto rideDto) {
        Ride existingRide = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));


        // Обновляем поля из DTO
        rideMapper.updateRide(rideDto, existingRide);
        // TODO: Синхронизировать с Order, если нужно (например, driverId)
        Order order = orderRepository.findById(existingRide.getOrderId()).orElse(null);
        if (order != null && rideDto.getDriverId() != null && !rideDto.getDriverId().equals(order.getDriverId())) {
            order.setDriverId(rideDto.getDriverId());
            orderRepository.save(order);
        }

        Ride updatedRide = rideRepository.save(existingRide);
        return rideMapper.toDto(updatedRide);
    }

    @Transactional
    public void deleteRide(UUID id) {
        if (!rideRepository.existsById(id)) {
            throw new RideNotFoundException(id);
        }
        // TODO: Что делать с Order? Удалять? Отменять?
        // TODO: Удалять ли UserRideHistory? (каскадное удаление в БД?)
        rideRepository.deleteById(id);
    }
}

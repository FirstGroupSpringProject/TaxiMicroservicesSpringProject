package com.orderrideservice.service;
import ch.qos.logback.classic.Logger;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.*;
import com.orderrideservice.exception.DriverNotFoundException;
import com.orderrideservice.exception.OrderNotFoundException;
import com.orderrideservice.exception.RideNotFoundException;
import com.orderrideservice.mapper.RideMapper;
import com.orderrideservice.repository.DriverCacheRepository;
import com.orderrideservice.repository.OrderRepository;
import com.orderrideservice.repository.RideRepository;
import com.aston.commonevents.dto.RideCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.orderrideservice.repository.UserRideHistoryRepository;
import com.orderrideservice.entity.UserRideHistory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с поездками.
 * Обеспечивает управление жизненным циклом поездок и интеграцию с другими сервисами.
 */
@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final OrderRepository orderRepository;
    private final DriverCacheRepository driverCacheRepository;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private KafkaTemplate<String, RideCompletedEvent> rideKafkaTemplate;
    private static final String RIDE_EVENTS_TOPIC = "ride-events";
    private static final String PAYMENT_REQUEST_TOPIC = "payment-requests";
    private final UserRideHistoryRepository userRideHistoryRepository;

    /**
     * Создает новую поездку.
     *
     * @param rideDto DTO с данными поездки
     * @return созданный RideDto
     * @throws DriverNotFoundException если водитель не найден
     * @throws IllegalStateException если водитель неактивен
     */
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


    /**
     * Завершает поездку и инициирует платеж.
     *
     * @param rideId UUID поездки
     * @return RideDto завершенной поездки
     * @throws RideNotFoundException если поездка не найдена
     * @throws OrderNotFoundException если заказ не найден
     */
    @Transactional
    public RideDto completeRide(UUID rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
        Order order = orderRepository.findById(ride.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ride.getOrderId()));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            Logger log = null;
            log.info("Order {} status set to COMPLETED", order.getId());

            BigDecimal rideAmount = calculateRideAmount(ride.getDistance());

            RideCompletedEvent event = new RideCompletedEvent(
                    ride.getId(),
                    ride.getOrderId(),
                    ride.getDriverId(),
                    order.getUserId(),
                    rideAmount
            );
            try {
                log.info("Sending RideCompletedEvent for ride id: {}", rideId);
                rideKafkaTemplate.send(RIDE_EVENTS_TOPIC, ride.getId().toString(), event);
            } catch (Exception e) {
                log.error("Failed to send RideCompletedEvent for ride id: {}", rideId, e);
            }
        }
        return rideMapper.toDto(ride);
    }

    /**
     * Рассчитывает стоимость поездки на основе расстояния.
     *
     * @param distance пройденное расстояние
     * @return сумма к оплате
     */
    private BigDecimal calculateRideAmount(Double distance) {
        if (distance == null || distance <= 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(distance).multiply(BigDecimal.valueOf(15.5)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * Получает поездку по идентификатору.
     *
     * @param id UUID поездки
     * @return Optional с RideDto, если поездка найдена
     */
    @Transactional(readOnly = true)
    public Optional<RideDto> getRideById(UUID id) {
        Logger log = null;
        log.debug("Finding ride by id: {}", id);
        return rideRepository.findById(id).map(rideMapper::toDto);
    }

    /**
     * Получает список всех поездок.
     *
     * @return список RideDto
     */
    @Transactional(readOnly = true)
    public List<RideDto> getAllRides() {
        Logger log = null;
        log.debug("Finding all rides");
        return rideRepository.findAll().stream()
                .map(rideMapper::toDto)
                .collect(Collectors.toList());
    }
    /**
     * Обновляет данные поездки.
     *
     * @param id UUID поездки
     * @param rideDto DTO с новыми данными
     * @return обновленный RideDto
     * @throws RideNotFoundException если поездка не найдена
     */
    @Transactional
    public RideDto updateRide(UUID id, RideDto rideDto) {
        Ride existingRide = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
        rideMapper.updateRide(rideDto, existingRide);
        Order order = orderRepository.findById(existingRide.getOrderId()).orElse(null);
        if (order != null && rideDto.getDriverId() != null && !rideDto.getDriverId().equals(order.getDriverId())) {
            order.setDriverId(rideDto.getDriverId());
            orderRepository.save(order);
        }

        Ride updatedRide = rideRepository.save(existingRide);
        return rideMapper.toDto(updatedRide);
    }
    /**
     * Удаляет поездку.
     *
     * @param id UUID поездки
     * @throws RideNotFoundException если поездка не найдена
     */
    @Transactional
    public void deleteRide(UUID id) {
        if (!rideRepository.existsById(id)) {
            throw new RideNotFoundException(id);
        }
        rideRepository.deleteById(id);
    }
}

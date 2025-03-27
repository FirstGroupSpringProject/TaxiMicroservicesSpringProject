package com.orderrideservice.service;

import com.orderrideservice.dto.OrderDto;
import com.orderrideservice.entity.Order;
import com.orderrideservice.exception.OrderNotFoundException;
import com.orderrideservice.mapper.OrderMapper;
import com.orderrideservice.repository.DriverCacheRepository;
import com.orderrideservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Важно для update/delete

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // Используем Slf4j для логирования
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DriverCacheRepository driverCacheRepository; // Для проверки доступности

    @Transactional // Важно для создания
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Attempting to create order for user: {}", orderDto.getUserId());
        // TODO: Добавить логику проверки доступности водителя из driverCacheRepository
        // Пример:
        // boolean driverAvailable = driverCacheRepository.findById(orderDto.getDriverId())
        //                                             .map(cache -> "AVAILABLE".equals(cache.getCurrentStatus()) && cache.isActive())
        //                                             .orElse(false);
        // if (!driverAvailable) {
        //    log.warn("Driver {} is not available or not found", orderDto.getDriverId());
        //    throw new RuntimeException("Driver not available"); // Или более специфичное исключение
        // }

        Order order = orderMapper.toEntity(orderDto);
        // Убедимся, что статус по умолчанию правильный, если не передан
        if (order.getStatus() == null) {
            order.setStatus(com.orderrideservice.entity.OrderStatus.IN_PROGRESS);
        }
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with id: {}", savedOrder.getId());
        return orderMapper.toDto(savedOrder);
    }

    @Transactional(readOnly = true) // Только чтение
    public Optional<OrderDto> getOrderById(UUID id) {
        log.debug("Finding order by id: {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    @Transactional(readOnly = true) // Только чтение
    public List<OrderDto> getAllOrders() {
        log.debug("Finding all orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional // Важно для обновления
    public OrderDto updateOrder(UUID id, OrderDto orderDto) {
        log.info("Attempting to update order with id: {}", id);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Обновляем поля из DTO в существующую сущность
        orderMapper.updateOrder(orderDto, existingOrder);

        // Пересохраняем обновленную сущность
        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Order updated successfully: {}", updatedOrder.getId());
        return orderMapper.toDto(updatedOrder);
    }

    @Transactional // Важно для удаления
    public void deleteOrder(UUID id) {
        log.info("Attempting to delete order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with id: {}", id);
    }
}
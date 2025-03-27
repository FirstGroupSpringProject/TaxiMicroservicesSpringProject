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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с заказами.
 * Предоставляет CRUD-операции и бизнес-логику для управления заказами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final DriverCacheRepository driverCacheRepository;

    /**
     * Создает новый заказ.
     *
     * @param orderDto DTO с данными заказа
     * @return созданный OrderDto
     */
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Attempting to create order for user: {}", orderDto.getUserId());

        Order order = orderMapper.toEntity(orderDto);

        if (order.getStatus() == null) {
            order.setStatus(com.orderrideservice.entity.OrderStatus.IN_PROGRESS);
        }
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with id: {}", savedOrder.getId());
        return orderMapper.toDto(savedOrder);
    }

    /**
     * Получает заказ по идентификатору.
     *
     * @param id UUID заказа
     * @return Optional с OrderDto, если заказ найден
     */
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(UUID id) {
        log.debug("Finding order by id: {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    /**
     * Получает список всех заказов.
     *
     * @return список OrderDto
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        log.debug("Finding all orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет существующий заказ.
     *
     * @param id UUID заказа
     * @param orderDto DTO с новыми данными заказа
     * @return обновленный OrderDto
     * @throws OrderNotFoundException если заказ не найден
     */
    @Transactional
    public OrderDto updateOrder(UUID id, OrderDto orderDto) {
        log.info("Attempting to update order with id: {}", id);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderMapper.updateOrder(orderDto, existingOrder);

        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Order updated successfully: {}", updatedOrder.getId());
        return orderMapper.toDto(updatedOrder);
    }

    /**
     * Удаляет заказ по идентификатору.
     *
     * @param id UUID заказа
     * @throws OrderNotFoundException если заказ не найден
     */
    @Transactional
    public void deleteOrder(UUID id) {
        log.info("Attempting to delete order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with id: {}", id);
    }
}
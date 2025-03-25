package com.orderrideservice.service;

import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.DriverCache;
import com.orderrideservice.entity.Ride;
import com.orderrideservice.event.RideEvent;
import com.orderrideservice.exception.DriverNotFoundException;
import com.orderrideservice.mapper.RideMapper;
import com.orderrideservice.repository.DriverCacheRepository;
import com.orderrideservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.orderrideservice.dto.OrderDto;
import com.orderrideservice.entity.Order;
import com.orderrideservice.mapper.OrderMapper;
import com.orderrideservice.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static Logger log = LoggerFactory.getLogger(OrderService.class);

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            log.info("No orders found");
            return Collections.emptyList();
        }
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

}

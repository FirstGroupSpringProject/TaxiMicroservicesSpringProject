package com.orderrideservice.controller;

import com.orderrideservice.dto.OrderDto;
import com.orderrideservice.exception.OrderNotFoundException;
import com.orderrideservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        LOG.info("Creating order: {}", orderDto);
        OrderDto createdOrder = orderService.createOrder(orderDto);
        LOG.info("Order created: {}", createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        LOG.info("Getting order by id: {}", id);
        return orderService.getOrderById(id)
                .map(orderDto -> {
                    LOG.info("Order found: {}", orderDto);
                    return ResponseEntity.ok(orderDto);
                })
                .orElseGet(() -> {
                    LOG.warn("Order not found with id: {}", id);
                    return ResponseEntity.notFound().build(); // Возвращаем 404 Not Found
                });
    }


    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        LOG.info("Getting all orders");
        List<OrderDto> orderDtoList = orderService.getAllOrders();
        if (orderDtoList.isEmpty()) {
            LOG.info("No orders found");
            return ResponseEntity.noContent().build();
        } else {
            LOG.info("Found {} orders", orderDtoList.size());
            return ResponseEntity.ok(orderDtoList);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable UUID id, @RequestBody OrderDto orderDto) {
        LOG.info("Updating order with id: {}, data: {}", id, orderDto);
        try {
            OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
            LOG.info("Order updated: {}", updatedOrder);
            return ResponseEntity.ok(updatedOrder);
        } catch (OrderNotFoundException e) {
            LOG.warn("Cannot update order, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error updating order with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        LOG.info("Deleting order with id: {}", id);
        try {
            orderService.deleteOrder(id);
            LOG.info("Order deleted with id: {}", id);
            return ResponseEntity.noContent().build(); // Возвращаем 204 No Content
        } catch (OrderNotFoundException e) {
            LOG.warn("Cannot delete order, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error deleting order with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
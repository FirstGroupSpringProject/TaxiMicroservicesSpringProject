package com.orderrideservice.kafka.consumer;

import com.orderrideservice.entity.Order;
import com.aston.commonevents.dto.PaymentStatusUpdatedEvent;
import com.orderrideservice.exception.OrderNotFoundException;
import com.orderrideservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "${spring.kafka.topics.payment-events:payment-events}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handlePaymentStatusUpdate(PaymentStatusUpdatedEvent event) {
        log.info("Received PaymentStatusUpdatedEvent for orderId: {}, paymentId: {}, status: {}",
                event.getOrderId(), event.getPaymentId(), event.getStatus());

        try {
            Order order = orderRepository.findById(event.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException(event.getOrderId()));

            if ("SUCCEEDED".equalsIgnoreCase(event.getStatus())) {
                order.setPaymentId(event.getPaymentId());
                orderRepository.save(order);
                log.info("Updated order {} with successful paymentId {}", order.getId(), event.getPaymentId());
            } else if ("FAILED".equalsIgnoreCase(event.getStatus())) {
                log.warn("Payment failed for order {}. PaymentId: {}", order.getId(), event.getPaymentId());
            }
        } catch (OrderNotFoundException e) {
            log.warn("Order not found while processing PaymentStatusUpdatedEvent: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("Error processing PaymentStatusUpdatedEvent for orderId: {}", event.getOrderId(), e);
        }
    }
}
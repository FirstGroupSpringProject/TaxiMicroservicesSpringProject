package com.aston.paymentbillingservice.kafka.consumer; // Твой пакет


import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.entity.PaymentStatus;
import com.aston.paymentbillingservice.repository.PaymentRepository; // Импорт репозитория
import com.aston.commonevents.dto.RideCompletedEvent; // ИЗМЕНЕНО: Импорт из общего модуля
import com.aston.paymentbillingservice.service.PaymentService;
import com.aston.paymentbillingservice.service.ServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestConsumer {

    private final ServiceInterface<PaymentDto> paymentService;
    private final PaymentRepository paymentRepository;
    @KafkaListener(topics = "${spring.kafka.topics.payment-requests:payment-requests}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handlePaymentRequest(RideCompletedEvent event) {
        log.info("Received Payment Request for orderId={}, rideId={}, amount={}",
                event.getOrderId(), event.getRideId(), event.getAmount());

        if (event.getOrderId() == null || event.getUserId() == null || event.getAmount() == null) {
            log.error("Invalid Payment Request received: missing orderId, userId or amount. RideId={}", event.getRideId());

        }

    }
}
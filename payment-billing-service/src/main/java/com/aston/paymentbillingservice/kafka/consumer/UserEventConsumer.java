package com.aston.paymentbillingservice.kafka.consumer;
import com.aston.paymentbillingservice.dto.InvoiceDto;
import com.aston.paymentbillingservice.entity.PaymentMethod;
import com.aston.paymentbillingservice.repository.InvoiceRepository;
import com.aston.commonevents.dto.UserCreatedEvent;
import com.aston.paymentbillingservice.service.ServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Kafka Consumer для обработки событий о пользователях.
 * Создает начальные счета для новых пользователей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {
    private final ServiceInterface<InvoiceDto> invoiceService;
    private final InvoiceRepository invoiceRepository;

    /**
     * Обрабатывает событие создания пользователя.
     *
     * @param event событие UserCreatedEvent
     */
    @KafkaListener(topics = "${spring.kafka.topics.user-events:user-events}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for userId={}", event.getUserId());

        if (event.getUserId() == null) {
            log.error("Invalid UserCreatedEvent received: missing userId.");
            return;
        }

        try {

            InvoiceDto newInvoice = new InvoiceDto();
            newInvoice.setUserId(event.getUserId());
            newInvoice.setPaymentMethod(PaymentMethod.CARD);
            newInvoice.setDueDate(LocalDateTime.now().plusYears(1));

            InvoiceDto savedInvoice = invoiceService.save(newInvoice);
            log.info("Created initial invoice {} for user {}", savedInvoice.getId(), event.getUserId());

        } catch (Exception e) {
            log.error("Error processing UserCreatedEvent for userId={}. Error: {}",
                    event.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Failed to process UserCreatedEvent: " + e.getMessage(), e);
        }
    }
}
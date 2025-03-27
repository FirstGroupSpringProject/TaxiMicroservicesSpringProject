package com.aston.paymentbillingservice.service;

import ch.qos.logback.classic.Logger;
import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.entity.Payment;
import com.aston.paymentbillingservice.entity.PaymentStatus;
import com.aston.paymentbillingservice.event.PaymentStatusUpdatedEvent;
import com.aston.paymentbillingservice.mapper.Mapper;
import com.aston.paymentbillingservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис PaymentService для управления платежами.
 * Предоставляет операции для работы с платежами такими как получение,
 * создание, обновление и удаление.
 */
@Service
public class PaymentService implements ServiceInterface<PaymentDto> {

    @Autowired
    private KafkaTemplate<String, PaymentStatusUpdatedEvent> paymentEventKafkaTemplate;
    private static final String PAYMENT_EVENTS_TOPIC = "payment-events";
    private final Mapper<Payment, PaymentDto> paymentMapper;
    private final PaymentRepository paymentRepository;

    /**
     * Конструктор класса PaymentService.
     *
     * @param paymentMapper     - Mapper для преобразования объектов Payment в PaymentDto и обратно
     * @param paymentRepository - репозиторий для работы с Payment
     */
    @Autowired
    public PaymentService(Mapper<Payment, PaymentDto> paymentMapper, PaymentRepository paymentRepository) {
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Получает список всех PaymentDto.
     *
     * @return список всех PaymentDto
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> findAll() {
        Logger log = null;
        log.debug("Finding all payments");
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает PaymentDto по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа PaymentDto, если найден
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDto> findById(UUID id) {
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    /**
     * Сохраняет новый Payment в репозитории.
     *
     * @param paymentDto - объект PaymentDto, который необходимо сохранить
     * @return сохранённый объект Optional типа PaymentDto
     */
    @Override
    @Transactional
    public PaymentDto save(PaymentDto paymentDto) {;
        Payment payment = paymentMapper.toEntity(paymentDto);
        payment.setStatus(PaymentStatus.PENDING);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toDto(savedPayment);
    }


    /**
     * Удаляет Payment по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа PaymentDto, если удаление прошло успешно
     */
    @Override
    @Transactional
    public Optional<PaymentDto> delete(UUID id) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            paymentRepository.deleteById(id);
            return paymentOpt.map(paymentMapper::toDto);
        } else {
;
            return Optional.empty();
        }
    }

    /**
     * Обновляет существующий Payment в репозитории.
     *
     * @param id         - идентификатор
     * @param paymentDto - новые данные для обновлёния PaymentDto
     * @return объект Optional типа PaymentDto, если обновление прошло успешно
     */
    @Override
    @Transactional
    public Optional<PaymentDto> update(UUID id, PaymentDto paymentDto) {

        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isEmpty()) {

            return Optional.empty();
        }
        Payment existingPayment = paymentOpt.get();
        boolean statusChanged = existingPayment.getStatus() != paymentDto.getStatus();

        existingPayment.setAmount(paymentDto.getAmount());
        existingPayment.setStatus(paymentDto.getStatus());

        Payment updatedPayment = paymentRepository.save(existingPayment);
        PaymentDto updatedDto = paymentMapper.toDto(updatedPayment);

        if (statusChanged) {
            sendPaymentStatusEvent(updatedPayment);
        }

        return Optional.of(updatedDto);
    }


    public Optional<PaymentDto> updatePaymentStatus(UUID paymentId, PaymentStatus newStatus) {

        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isEmpty()) {

            return Optional.empty();
        }

        Payment payment = paymentOpt.get();
        if (payment.getStatus() == newStatus) {
            return Optional.of(paymentMapper.toDto(payment));
        }

        payment.setStatus(newStatus);
        Payment updatedPayment = paymentRepository.save(payment);

        sendPaymentStatusEvent(updatedPayment);

        return Optional.of(paymentMapper.toDto(updatedPayment));
    }

    private void sendPaymentStatusEvent(Payment payment) {
        PaymentStatusUpdatedEvent event = new PaymentStatusUpdatedEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getStatus().name()
        );

    }
}

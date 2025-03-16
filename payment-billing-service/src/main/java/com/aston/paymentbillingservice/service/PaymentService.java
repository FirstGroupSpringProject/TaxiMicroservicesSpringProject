package com.aston.paymentbillingservice.service;

import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.entity.Payment;
import com.aston.paymentbillingservice.mapper.Mapper;
import com.aston.paymentbillingservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис PaymentService для управления платежами.
 * Предоставляет операции для работы с платежами такими как получение,
 * создание, обновление и удаление.
 */
@Service
public class PaymentService implements ServiceInterface<PaymentDto> {

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
    public List<PaymentDto> findAll() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDto> paymentsDto = new ArrayList<>();
        for (Payment payment : payments) {
            paymentsDto.add(paymentMapper.toDto(payment));
        }
        return paymentsDto;
    }

    /**
     * Получает PaymentDto по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа PaymentDto, если найден
     */
    @Override
    public Optional<PaymentDto> findById(UUID id) {
        Optional<Payment> paymentById = paymentRepository.findById(id);
        return paymentById.map(paymentMapper::toDto);
    }

    /**
     * Сохраняет новый Payment в репозитории.
     *
     * @param paymentDto - объект PaymentDto, который необходимо сохранить
     * @return сохранённый объект Optional типа PaymentDto
     */
    @Override
    public PaymentDto save(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toEntity(paymentDto);
        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Удаляет Payment по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа PaymentDto, если удаление прошло успешно
     */
    @Override
    public Optional<PaymentDto> delete(UUID id) {
        Optional<Payment> paymentById = paymentRepository.findById(id);
        paymentRepository.deleteById(id);
        return paymentById.map(paymentMapper::toDto);
    }

    /**
     * Обновляет существующий Payment в репозитории.
     *
     * @param id         - идентификатор
     * @param paymentDto - новые данные для обновлёния PaymentDto
     * @return объект Optional типа PaymentDto, если обновление прошло успешно
     */
    @Override
    public Optional<PaymentDto> update(UUID id, PaymentDto paymentDto) {
        Optional<Payment> payment = paymentRepository.findById(id)
                .map(p -> {
                    p.setAmount(paymentDto.getAmount());
                    p.setStatus(paymentDto.getStatus());
                    return paymentRepository.save(p);
                });
        return payment.map(paymentMapper::toDto);
    }
}
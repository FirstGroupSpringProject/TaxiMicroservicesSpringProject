package com.aston.paymentbillingservice.mapper;

import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.entity.Payment;
import org.springframework.stereotype.Component;

/**
 * Класс PaymentMapper отвечает за преобразование между объектами типа PaymentDto и Payment.
 * Данный класс предоставляет методы для преобразования данных из
 * объектов передачи данных (PaymentDto) в сущности (Payment) и обратно.
 */
@Component
public class PaymentMapper implements Mapper<Payment, PaymentDto> {
    @Override
    public PaymentDto toDto(Payment payment) {
        return new PaymentDto(payment.getId(), payment.getOrderId(), payment.getUserId(),
                payment.getAmount(), payment.getStatus());
    }
    /**
     * Преобразует объект типа PaymentDto в объект типа Payment.
     *
     * @param paymentDto - объект PaymentDto, который необходимо преобразовать
     * @return преобразованный объект Payment
     */
    @Override
    public Payment toEntity(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setId(paymentDto.getId());
        payment.setOrderId(paymentDto.getOrderId());
        payment.setUserId(paymentDto.getUserId());
        payment.setAmount(paymentDto.getAmount());
        payment.setStatus(paymentDto.getStatus());
        return payment;
    }
}

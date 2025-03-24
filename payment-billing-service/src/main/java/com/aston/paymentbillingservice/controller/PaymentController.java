package com.aston.paymentbillingservice.controller;

import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.exception.BadRequestException;
import com.aston.paymentbillingservice.exception.NotFoundException;
import com.aston.paymentbillingservice.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления платежами.
 * Предоставляет REST API для выполнения операций с платежами.
 */
@RestController
@RequestMapping("/api/v1.0")
public class PaymentController {

    private final ServiceInterface<PaymentDto> paymentService;

    /**
     * Конструктор класса PaymentController.
     *
     * @param paymentService сервис, выполняющий операции с платежами
     */
    @Autowired
    public PaymentController(ServiceInterface<PaymentDto> paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Получает список всех платежей.
     *
     * @return список объектов PaymentDto
     */
    @GetMapping("/payments")
    public List<PaymentDto> getPayments() {
        return paymentService.findAll();
    }

    /**
     * Получает платеж по заданному идентификатору.
     *
     * @param paymentId - индентификатор платежа
     * @return объект PaymentDto
     * @throws NotFoundException если платеж с указанным идентификатором не найден
     */
    @GetMapping("/payments/{paymentId}")
    public PaymentDto getPaymentById(@PathVariable UUID paymentId) {
        return paymentService.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(paymentId));
    }

    /**
     * Создает новый платеж.
     *
     * @param paymentDto - объект PaymentDto с данными для создания платежа
     * @return созданный объект PaymentDto
     */
    @PostMapping("/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@RequestBody PaymentDto paymentDto) {
        return paymentService.save(paymentDto);
    }

    /**
     * Удаляет платеж по заданному идентификатору.
     *
     * @param paymentId - идентификатор платежа, который нужно удалить
     * @return объект PaymentDto с информацией об удаленном платеже
     * @throws NotFoundException если платеж с указанным идентификатором не найден
     */
    @DeleteMapping("/payments/{paymentId}")
    public PaymentDto deletePayment(@PathVariable UUID paymentId) {
        return paymentService.delete(paymentId)
                .orElseThrow(() -> new NotFoundException(paymentId));
    }

    /**
     * Обновляет информацию о платеже по заданному идентификатору.
     *
     * @param paymentId  - идентификатор платежа, который нужно обновить
     * @param paymentDto - объект PaymentDto с новыми данными
     * @return обновленный объект PaymentDto
     * @throws BadRequestException если обновление не удалось
     */
    @PutMapping("/payments/{paymentId}")
    public PaymentDto updatePayment(@PathVariable UUID paymentId, @RequestBody PaymentDto paymentDto) {
        return paymentService.update(paymentId, paymentDto)
                .orElseThrow(() -> new BadRequestException(paymentId));
    }
}

package com.aston.paymentbillingservice.controller;

import com.aston.paymentbillingservice.dto.InvoiceDto;
import com.aston.paymentbillingservice.exception.BadRequestException;
import com.aston.paymentbillingservice.exception.NotFoundException;
import com.aston.paymentbillingservice.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления счетами.
 * Предоставляет REST API для выполнения операций с счетами.
 */
@RestController
@RequestMapping("/api/v1.0")
public class InvoiceController {

    private final ServiceInterface<InvoiceDto> invoiceService;

    /**
     * Конструктор класса InvoiceController.
     *
     * @param invoiceService сервис, выполняющий операции с счетами
     */
    @Autowired
    public InvoiceController(ServiceInterface<InvoiceDto> invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Получает список всех счетов.
     *
     * @return список объектов InvoiceDto
     */
    @GetMapping("/invoices")
    public List<InvoiceDto> getInvoices() {
        return invoiceService.findAll();
    }

    /**
     * Получает счёт по заданному идентификатору.
     *
     * @param invoiceId - индентификатор счёта
     * @return объект InvoiceDto
     * @throws NotFoundException если счёт с указанным идентификатором не найден
     */
    @GetMapping("/invoices/{invoiceId}")
    public InvoiceDto getInvoiceById(@PathVariable UUID invoiceId) {
        return invoiceService.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException(invoiceId));
    }

    /**
     * Создает новый счёт.
     *
     * @param invoiceDto - объект InvoiceDto с данными для создания счёт
     * @param userId     - идентификатор пользователя
     * @return созданный объект InvoiceDto
     */
    @PostMapping("/invoices/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDto createInvoice(@RequestBody InvoiceDto invoiceDto, @PathVariable UUID userId) {
        invoiceDto.setUserId(userId);
        invoiceDto.setDueDate(LocalDateTime.now());
        return invoiceService.save(invoiceDto);
    }

    /**
     * Удаляет счёт по заданному идентификатору.
     *
     * @param invoiceId - идентификатор счёта, который нужно удалить
     * @return объект InvoiceDto с информацией о удаленном счёте
     * @throws NotFoundException если счёт с указанным идентификатором не найден
     */
    @DeleteMapping("/invoices/{invoiceId}")
    public InvoiceDto deleteInvoice(@PathVariable UUID invoiceId) {
        return invoiceService.delete(invoiceId)
                .orElseThrow(() -> new NotFoundException(invoiceId));
    }

    /**
     * Обновляет информацию о счёте по заданному идентификатору.
     *
     * @param invoiceId  - идентификатор счёта, который нужно обновить
     * @param invoiceDto - объект InvoiceDto с новыми данными
     * @param userId     - идентификатор пользователя
     * @return обновленный объект InvoiceDto
     * @throws BadRequestException если обновление не удалось
     */
    @PutMapping("/invoices/{invoiceId}/{userId}")
    public InvoiceDto updateInvoice(@PathVariable UUID invoiceId, @RequestBody InvoiceDto invoiceDto,
                                    @PathVariable UUID userId) {
        invoiceDto.setUserId(userId);
        invoiceDto.setDueDate(LocalDateTime.now());
        return invoiceService.update(invoiceId, invoiceDto)
                .orElseThrow(() -> new BadRequestException(invoiceId));
    }
}

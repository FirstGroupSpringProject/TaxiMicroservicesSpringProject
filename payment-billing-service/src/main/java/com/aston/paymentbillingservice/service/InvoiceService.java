package com.aston.paymentbillingservice.service;

import com.aston.paymentbillingservice.dto.InvoiceDto;
import com.aston.paymentbillingservice.entity.Invoice;
import com.aston.paymentbillingservice.mapper.Mapper;
import com.aston.paymentbillingservice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис InvoiceService для управления счетами.
 * Предоставляет операции для работы с платежами такими как получение,
 * создание, обновление и удаление.
 */
@Service
public class InvoiceService implements ServiceInterface<InvoiceDto> {

    private final Mapper<Invoice, InvoiceDto> invoiceMapper;
    private final InvoiceRepository invoiceRepository;

    /**
     * Конструктор класса InvoiceService.
     *
     * @param invoiceMapper     - Mapper для преобразования объектов Invoice в InvoiceDto и обратно
     * @param invoiceRepository - репозиторий для работы с Invoice
     */
    @Autowired
    public InvoiceService(Mapper<Invoice, InvoiceDto> invoiceMapper, InvoiceRepository invoiceRepository) {
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Получает список всех InvoiceDto.
     *
     * @return список всех InvoiceDto
     */
    @Override
    public List<InvoiceDto> findAll() {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<InvoiceDto> invoicesDto = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoicesDto.add(invoiceMapper.toDto(invoice));
        }
        return invoicesDto;
    }

    /**
     * Получает InvoiceDto по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа InvoiceDto, если найден
     */
    @Override
    public Optional<InvoiceDto> findById(UUID id) {
        Optional<Invoice> invoiceById = invoiceRepository.findById(id);
        return invoiceById.map(invoiceMapper::toDto);
    }

    /**
     * Сохраняет новый Invoice в репозитории.
     *
     * @param invoiceDto - объект InvoiceDto, который необходимо сохранить
     * @return сохранённый объект Optional типа InvoiceDto
     */
    @Override
    public InvoiceDto save(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.toEntity(invoiceDto);
        invoiceRepository.save(invoice);
        return invoiceMapper.toDto(invoice);
    }

    /**
     * Удаляет Invoice по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа InvoiceDto, если удаление прошло успешно
     */
    @Override
    public Optional<InvoiceDto> delete(UUID id) {
        Optional<Invoice> invoiceById = invoiceRepository.findById(id);
        invoiceRepository.deleteById(id);
        return invoiceById.map(invoiceMapper::toDto);
    }

    /**
     * Обновляет существующий Invoice в репозитории.
     *
     * @param id         - идентификатор
     * @param invoiceDto - новые данные для обновлёния InvoiceDto
     * @return объект Optional типа InvoiceDto, если обновление прошло успешно
     */
    @Override
    public Optional<InvoiceDto> update(UUID id, InvoiceDto invoiceDto) {
        Optional<Invoice> invoice = invoiceRepository.findById(id)
                .map(i -> {
                    i.setUserId(invoiceDto.getUserId());
                    i.setPaymentMethod(invoiceDto.getPaymentMethod());
                    i.setDueDate(invoiceDto.getDueDate());
                    return invoiceRepository.save(i);
                });
        return invoice.map(invoiceMapper::toDto);
    }
}
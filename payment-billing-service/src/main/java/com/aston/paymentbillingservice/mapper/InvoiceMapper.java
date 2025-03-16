package com.aston.paymentbillingservice.mapper;

import com.aston.paymentbillingservice.dto.InvoiceDto;
import com.aston.paymentbillingservice.entity.Invoice;
import org.springframework.stereotype.Component;

/**
 * Класс InvoiceMapper отвечает за преобразование между объектами типа InvoiceDto и Invoice.
 * Данный класс предоставляет методы для преобразования данных из
 * объектов передачи данных (InvoiceDto) в сущности (Invoice) и обратно.
 */
@Component
public class InvoiceMapper implements Mapper<Invoice, InvoiceDto> {

    /**
     * Преобразует объект типа Invoice в объект типа InvoiceDto.
     *
     * @param invoice - объект Invoice, который необходимо преобразовать
     * @return преобразованный объект InvoiceDto
     */
    @Override
    public InvoiceDto toDto(Invoice invoice) {
        return new InvoiceDto(invoice.getId(), invoice.getUserId(),
                invoice.getPaymentMethod(), invoice.getDueDate());
    }

    /**
     * Преобразует объект типа InvoiceDto в объект типа Invoice.
     *
     * @param invoiceDto - объект InvoiceDto, который необходимо преобразовать
     * @return преобразованный объект Invoice
     */
    @Override
    public Invoice toEntity(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.getId());
        invoice.setUserId(invoiceDto.getUserId());
        invoice.setPaymentMethod(invoiceDto.getPaymentMethod());
        invoice.setDueDate(invoiceDto.getDueDate());
        return invoice;
    }
}

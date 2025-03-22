package com.aston.paymentbillingservice.service;

import com.aston.paymentbillingservice.dto.InvoiceDto;
import com.aston.paymentbillingservice.entity.Invoice;
import com.aston.paymentbillingservice.entity.PaymentMethod;
import com.aston.paymentbillingservice.mapper.Mapper;
import com.aston.paymentbillingservice.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    private static final UUID ID_PARAMETER = UUID.fromString("b8b20b96-3cc2-4c32-95a4-3e1c1e01dd0c");
    private static final UUID ID_USER_PARAMETER = UUID.fromString("3f9611f4-f29e-4b4b-bfbf-602f46a36192");
    private static final PaymentMethod PAYMENT_METHOD_PARAMETER = PaymentMethod.CARD;
    private static final LocalDateTime DUE_DATE_PARAMETER = LocalDateTime.parse("2025-02-14T15:30:40.811420500");
    @Mock
    private Mapper<Invoice, InvoiceDto> invoiceMapper;
    @Mock
    private InvoiceRepository invoiceRepository;
    @InjectMocks
    private InvoiceService invoiceService;
    private Invoice invoiceExpected;
    private InvoiceDto invoiceDtoExpected;

    @BeforeEach
    void setUp() {
        invoiceExpected = new Invoice();
        invoiceExpected.setId(ID_PARAMETER);
        invoiceExpected.setUserId(ID_USER_PARAMETER);
        invoiceExpected.setPaymentMethod(PAYMENT_METHOD_PARAMETER);
        invoiceExpected.setDueDate(DUE_DATE_PARAMETER);

        invoiceDtoExpected = new InvoiceDto();
        invoiceDtoExpected.setId(ID_PARAMETER);
        invoiceDtoExpected.setUserId(ID_USER_PARAMETER);
        invoiceDtoExpected.setPaymentMethod(PAYMENT_METHOD_PARAMETER);
    }

    @Test
    void findById() {
        when(invoiceRepository.findById(ID_PARAMETER)).thenReturn(Optional.of(invoiceExpected));
        when(invoiceMapper.toDto(invoiceExpected)).thenReturn(invoiceDtoExpected);

        Optional<InvoiceDto> invoiceDtoActual = invoiceService.findById(ID_PARAMETER);

        assertTrue(invoiceDtoActual.isPresent());
        assertEquals(invoiceDtoExpected, invoiceDtoActual.get());
        verify(invoiceRepository).findById(ID_PARAMETER);
        verify(invoiceMapper).toDto(invoiceExpected);
    }

    @Test
    void findByIdNotExist() {
        when(invoiceRepository.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        Optional<InvoiceDto> invoiceDtoActual = invoiceService.findById(ID_PARAMETER);

        assertTrue(invoiceDtoActual.isEmpty());
        verify(invoiceRepository).findById(ID_PARAMETER);
        verify(invoiceMapper, never()).toDto(any(Invoice.class));
    }

    @Test
    void save() {
        when(invoiceMapper.toEntity(invoiceDtoExpected)).thenReturn(invoiceExpected);
        when(invoiceRepository.save(invoiceExpected)).thenReturn(invoiceExpected);
        when(invoiceMapper.toDto(invoiceExpected)).thenReturn(invoiceDtoExpected);

        InvoiceDto invoiceDtoActual = invoiceService.save(invoiceDtoExpected);

        assertNotNull(invoiceDtoActual);
        assertEquals(invoiceDtoExpected.getId(), invoiceDtoActual.getId());
        assertEquals(invoiceDtoExpected.getUserId(), invoiceDtoActual.getUserId());
        assertEquals(invoiceDtoExpected.getPaymentMethod(), invoiceDtoActual.getPaymentMethod());
        assertEquals(invoiceDtoExpected.getDueDate(), invoiceDtoActual.getDueDate());
        verify(invoiceMapper).toEntity(invoiceDtoExpected);
        verify(invoiceRepository).save(invoiceExpected);
    }

    @Test
    void delete() {
        when(invoiceRepository.findById(ID_PARAMETER)).thenReturn(Optional.of(invoiceExpected));
        when(invoiceMapper.toDto(invoiceExpected)).thenReturn(invoiceDtoExpected);

        Optional<InvoiceDto> invoiceDtoActual = invoiceService.delete(ID_PARAMETER);

        assertTrue(invoiceDtoActual.isPresent());

        assertEquals(invoiceDtoExpected, invoiceDtoActual.get());
        assertEquals(invoiceDtoExpected.getId(), invoiceDtoActual.get().getId());
        assertEquals(invoiceDtoExpected.getUserId(), invoiceDtoActual.get().getUserId());
        assertEquals(invoiceDtoExpected.getPaymentMethod(), invoiceDtoActual.get().getPaymentMethod());
        assertEquals(invoiceDtoExpected.getDueDate(), invoiceDtoActual.get().getDueDate());
        verify(invoiceMapper).toDto(invoiceExpected);
    }

    @Test
    void deleteNotExist() {
        when(invoiceRepository.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        Optional<InvoiceDto> invoiceDtoActual = invoiceService.delete(ID_PARAMETER);

        assertTrue(invoiceDtoActual.isEmpty());
        verify(invoiceRepository).findById(ID_PARAMETER);
        verify(invoiceMapper, never()).toDto(any(Invoice.class));
    }

    @Test
    void update() {
        when(invoiceRepository.findById(ID_PARAMETER)).thenReturn(Optional.of(invoiceExpected));
        when(invoiceRepository.save(invoiceExpected)).thenReturn(invoiceExpected);
        when(invoiceMapper.toDto(invoiceExpected)).thenReturn(invoiceDtoExpected);

        Optional<InvoiceDto> invoiceDtoActual = invoiceService.update(ID_PARAMETER, invoiceDtoExpected);

        assertTrue(invoiceDtoActual.isPresent());
        assertEquals(invoiceDtoExpected, invoiceDtoActual.get());
        assertEquals(invoiceDtoExpected.getId(), invoiceDtoActual.get().getId());
        assertEquals(invoiceDtoExpected.getUserId(), invoiceDtoActual.get().getUserId());
        assertEquals(invoiceDtoExpected.getPaymentMethod(), invoiceDtoActual.get().getPaymentMethod());
        assertEquals(invoiceDtoExpected.getDueDate(), invoiceDtoActual.get().getDueDate());
        verify(invoiceMapper).toDto(invoiceExpected);
        verify(invoiceRepository).save(invoiceExpected);
    }

    @Test
    void updateNotExist() {
        when(invoiceRepository.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        Optional<InvoiceDto> invoiceDtoActual = invoiceService.update(ID_PARAMETER, invoiceDtoExpected);

        assertTrue(invoiceDtoActual.isEmpty());
        verify(invoiceRepository).findById(ID_PARAMETER);
        verify(invoiceMapper, never()).toDto(any(Invoice.class));
    }
}
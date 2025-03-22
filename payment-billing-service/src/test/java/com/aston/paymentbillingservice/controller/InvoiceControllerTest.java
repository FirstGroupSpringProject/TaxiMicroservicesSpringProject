package com.aston.paymentbillingservice.controller;

import com.aston.paymentbillingservice.dto.InvoiceDto;
import com.aston.paymentbillingservice.entity.PaymentMethod;
import com.aston.paymentbillingservice.exception.BadRequestException;
import com.aston.paymentbillingservice.exception.NotFoundException;
import com.aston.paymentbillingservice.service.ServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerTest {

    private static final UUID ID_PARAMETER = UUID.fromString("b8b20b96-3cc2-4c32-95a4-3e1c1e01dd0c");
    private static final UUID ID_USER_PARAMETER = UUID.fromString("3f9611f4-f29e-4b4b-bfbf-602f46a36192");
    private static final PaymentMethod PAYMENT_METHOD_PARAMETER = PaymentMethod.CARD;
    private static final LocalDateTime DUE_DATE_PARAMETER = LocalDateTime.parse("2025-02-14T15:30:40.811420500");
    @Mock
    private ServiceInterface<InvoiceDto> invoiceService;
    @InjectMocks
    private InvoiceController invoiceController;
    private InvoiceDto invoiceDtoExpected;

    @BeforeEach
    void setUp() {
        invoiceDtoExpected = new InvoiceDto();
        invoiceDtoExpected.setId(ID_PARAMETER);
        invoiceDtoExpected.setUserId(ID_USER_PARAMETER);
        invoiceDtoExpected.setPaymentMethod(PAYMENT_METHOD_PARAMETER);
        invoiceDtoExpected.setDueDate(DUE_DATE_PARAMETER);
    }

    @Test
    void getInvoiceById() {
        when(invoiceService.findById(ID_PARAMETER)).thenReturn(Optional.of(invoiceDtoExpected));

        InvoiceDto invoiceDtoActual = invoiceController.getInvoiceById(ID_PARAMETER);

        assertEquals(invoiceDtoExpected, invoiceDtoActual);
        verify(invoiceService, times(1)).findById(ID_PARAMETER);
    }

    @Test
    void getInvoiceByIdNotFound() {
        when(invoiceService.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invoiceController.getInvoiceById(ID_PARAMETER));
        verify(invoiceService, times(1)).findById(ID_PARAMETER);
    }

    @Test
    void createInvoice() {
        when(invoiceService.save(invoiceDtoExpected)).thenReturn(invoiceDtoExpected);

        InvoiceDto invoiceDtoActual = invoiceController.createInvoice(invoiceDtoExpected, ID_USER_PARAMETER);

        assertEquals(invoiceDtoExpected, invoiceDtoActual);
        verify(invoiceService, times(1)).save(invoiceDtoExpected);
    }

    @Test
    void deleteInvoice() {
        when(invoiceService.delete(ID_PARAMETER)).thenReturn(Optional.of(invoiceDtoExpected));

        InvoiceDto invoiceDtoActual = invoiceController.deleteInvoice(ID_PARAMETER);

        assertEquals(invoiceDtoExpected, invoiceDtoActual);
        verify(invoiceService, times(1)).delete(ID_PARAMETER);
    }

    @Test
    void deleteInvoiceNotFound() {
        when(invoiceService.delete(ID_PARAMETER)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invoiceController.deleteInvoice(ID_PARAMETER));
        verify(invoiceService, times(1)).delete(ID_PARAMETER);
    }

    @Test
    void updateInvoice() {
        when(invoiceService.update(ID_PARAMETER, invoiceDtoExpected)).thenReturn(Optional.of(invoiceDtoExpected));

        InvoiceDto invoiceDtoActual = invoiceController.updateInvoice(ID_PARAMETER, invoiceDtoExpected, ID_USER_PARAMETER);

        assertEquals(invoiceDtoExpected, invoiceDtoActual);
        verify(invoiceService, times(1)).update(ID_PARAMETER, invoiceDtoExpected);
    }

    @Test
    void updateInvoiceNotFound() {
        when(invoiceService.update(ID_PARAMETER, invoiceDtoExpected)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () ->
                invoiceController.updateInvoice(ID_PARAMETER, invoiceDtoExpected, ID_USER_PARAMETER));
        verify(invoiceService, times(1)).update(ID_PARAMETER, invoiceDtoExpected);
    }
}
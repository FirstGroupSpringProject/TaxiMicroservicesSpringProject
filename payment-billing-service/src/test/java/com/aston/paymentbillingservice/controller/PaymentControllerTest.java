package com.aston.paymentbillingservice.controller;

import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.entity.PaymentStatus;
import com.aston.paymentbillingservice.exception.BadRequestException;
import com.aston.paymentbillingservice.exception.NotFoundException;
import com.aston.paymentbillingservice.service.ServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private static final UUID ID_PARAMETER = UUID.fromString("b8b20b96-3cc2-4c32-95a4-3e1c1e01dd0c");
    private static final BigDecimal AMOUNT_PARAMETER = BigDecimal.valueOf(350.6);
    private static final PaymentStatus PAYMENT_STATUS_PARAMETER = PaymentStatus.SUCCEEDED;
    @Mock
    private ServiceInterface<PaymentDto> paymentService;
    @InjectMocks
    private PaymentController paymentController;
    private PaymentDto paymentDtoExpected;

    @BeforeEach
    void setUp() {
        paymentDtoExpected = new PaymentDto();
        paymentDtoExpected.setId(ID_PARAMETER);
        paymentDtoExpected.setAmount(AMOUNT_PARAMETER);
        paymentDtoExpected.setStatus(PAYMENT_STATUS_PARAMETER);
    }

    @Test
    void getPaymentById() {
        when(paymentService.findById(ID_PARAMETER)).thenReturn(Optional.of(paymentDtoExpected));

        PaymentDto paymentDtoActual = paymentController.getPaymentById(ID_PARAMETER);

        assertEquals(paymentDtoExpected, paymentDtoActual);
        verify(paymentService, times(1)).findById(ID_PARAMETER);
    }

    @Test
    void getPaymentByIdNotFound() {
        when(paymentService.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentController.getPaymentById(ID_PARAMETER));
        verify(paymentService, times(1)).findById(ID_PARAMETER);
    }

    @Test
    void createPayment() {
        when(paymentService.save(paymentDtoExpected)).thenReturn(paymentDtoExpected);

        PaymentDto paymentDtoActual = paymentController.createPayment(paymentDtoExpected);

        assertEquals(paymentDtoExpected, paymentDtoActual);
        verify(paymentService, times(1)).save(paymentDtoExpected);
    }

    @Test
    void deletePayment() {
        when(paymentService.delete(ID_PARAMETER)).thenReturn(Optional.of(paymentDtoExpected));

        PaymentDto paymentDtoActual = paymentController.deletePayment(ID_PARAMETER);

        assertEquals(paymentDtoExpected, paymentDtoActual);
        verify(paymentService, times(1)).delete(ID_PARAMETER);
    }

    @Test
    void deletePaymentNotFound() {
        when(paymentService.delete(ID_PARAMETER)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentController.deletePayment(ID_PARAMETER));
        verify(paymentService, times(1)).delete(ID_PARAMETER);
    }

    @Test
    void updatePayment() {
        when(paymentService.update(ID_PARAMETER, paymentDtoExpected)).thenReturn(Optional.of(paymentDtoExpected));

        PaymentDto paymentDtoActual = paymentController.updatePayment(ID_PARAMETER, paymentDtoExpected);

        assertEquals(paymentDtoExpected, paymentDtoActual);
        verify(paymentService, times(1)).update(ID_PARAMETER, paymentDtoExpected);
    }

    @Test
    void updatePaymentNotFound() {
        when(paymentService.update(ID_PARAMETER, paymentDtoExpected)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () ->
                paymentController.updatePayment(ID_PARAMETER, paymentDtoExpected));
        verify(paymentService, times(1)).update(ID_PARAMETER, paymentDtoExpected);
    }
}
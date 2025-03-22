package com.aston.paymentbillingservice.service;

import com.aston.paymentbillingservice.dto.PaymentDto;
import com.aston.paymentbillingservice.entity.Payment;
import com.aston.paymentbillingservice.entity.PaymentStatus;
import com.aston.paymentbillingservice.mapper.Mapper;
import com.aston.paymentbillingservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static final UUID ID_PARAMETER = UUID.fromString("b8b20b96-3cc2-4c32-95a4-3e1c1e01dd0c");
    private static final BigDecimal AMOUNT_PARAMETER = BigDecimal.valueOf(350.6);
    private static final PaymentStatus PAYMENT_STATUS_PARAMETER = PaymentStatus.SUCCEEDED;
    @Mock
    private Mapper<Payment, PaymentDto> paymentMapper;
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentService paymentService;
    private Payment paymentExpected;
    private PaymentDto paymentDtoExpected;

    @BeforeEach
    void setUp() {
        paymentExpected = new Payment();
        paymentExpected.setId(ID_PARAMETER);
        paymentExpected.setAmount(AMOUNT_PARAMETER);
        paymentExpected.setStatus(PAYMENT_STATUS_PARAMETER);

        paymentDtoExpected = new PaymentDto();
        paymentDtoExpected.setId(ID_PARAMETER);
        paymentDtoExpected.setAmount(AMOUNT_PARAMETER);
        paymentDtoExpected.setStatus(PAYMENT_STATUS_PARAMETER);
    }

    @Test
    void findById() {
        when(paymentRepository.findById(ID_PARAMETER)).thenReturn(Optional.of(paymentExpected));
        when(paymentMapper.toDto(paymentExpected)).thenReturn(paymentDtoExpected);

        Optional<PaymentDto> paymentDtoActual = paymentService.findById(ID_PARAMETER);

        assertTrue(paymentDtoActual.isPresent());
        assertEquals(paymentDtoExpected, paymentDtoActual.get());
        verify(paymentRepository).findById(ID_PARAMETER);
        verify(paymentMapper).toDto(paymentExpected);
    }

    @Test
    void findByIdNotExist() {
        when(paymentRepository.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        Optional<PaymentDto> paymentDtoActual = paymentService.findById(ID_PARAMETER);

        assertTrue(paymentDtoActual.isEmpty());
        verify(paymentRepository).findById(ID_PARAMETER);
        verify(paymentMapper, never()).toDto(any(Payment.class)); // Mapper не был вызван
    }

    @Test
    void save() {
        when(paymentMapper.toEntity(paymentDtoExpected)).thenReturn(paymentExpected);
        when(paymentRepository.save(paymentExpected)).thenReturn(paymentExpected);
        when(paymentMapper.toDto(paymentExpected)).thenReturn(paymentDtoExpected);

        PaymentDto paymentDtoActual = paymentService.save(paymentDtoExpected);

        assertNotNull(paymentDtoActual);
        assertEquals(paymentDtoExpected.getId(), paymentDtoActual.getId());
        assertEquals(paymentDtoExpected.getAmount(), paymentDtoActual.getAmount());
        assertEquals(paymentDtoExpected.getStatus(), paymentDtoActual.getStatus());
        verify(paymentMapper).toEntity(paymentDtoExpected);
        verify(paymentRepository).save(paymentExpected);
    }

    @Test
    void delete() {
        when(paymentRepository.findById(ID_PARAMETER)).thenReturn(Optional.of(paymentExpected));
        when(paymentMapper.toDto(paymentExpected)).thenReturn(paymentDtoExpected);

        Optional<PaymentDto> paymentDtoActual = paymentService.delete(ID_PARAMETER);

        assertTrue(paymentDtoActual.isPresent());
        assertEquals(paymentDtoExpected, paymentDtoActual.get());
        assertEquals(paymentDtoExpected.getId(), paymentDtoActual.get().getId());
        assertEquals(paymentDtoExpected.getAmount(), paymentDtoActual.get().getAmount());
        assertEquals(paymentDtoExpected.getStatus(), paymentDtoActual.get().getStatus());
        verify(paymentMapper).toDto(paymentExpected);
    }

    @Test
    void deleteNotExist() {
        when(paymentRepository.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        Optional<PaymentDto> paymentDtoActual = paymentService.delete(ID_PARAMETER);

        assertTrue(paymentDtoActual.isEmpty());
        verify(paymentRepository).findById(ID_PARAMETER);
        verify(paymentMapper, never()).toDto(any(Payment.class));
    }

    @Test
    void update() {
        when(paymentRepository.findById(ID_PARAMETER)).thenReturn(Optional.of(paymentExpected));
        when(paymentRepository.save(paymentExpected)).thenReturn(paymentExpected);
        when(paymentMapper.toDto(paymentExpected)).thenReturn(paymentDtoExpected);

        Optional<PaymentDto> paymentDtoActual = paymentService.update(ID_PARAMETER, paymentDtoExpected);

        assertTrue(paymentDtoActual.isPresent());
        assertEquals(paymentDtoExpected, paymentDtoActual.get());
        assertEquals(paymentDtoExpected.getId(), paymentDtoActual.get().getId());
        assertEquals(paymentDtoExpected.getAmount(), paymentDtoActual.get().getAmount());
        assertEquals(paymentDtoExpected.getStatus(), paymentDtoActual.get().getStatus());
        verify(paymentMapper).toDto(paymentExpected);
        verify(paymentRepository).save(paymentExpected);
    }

    @Test
    void updateNotExist() {
        when(paymentRepository.findById(ID_PARAMETER)).thenReturn(Optional.empty());

        Optional<PaymentDto> paymentDtoActual = paymentService.update(ID_PARAMETER, paymentDtoExpected);

        assertTrue(paymentDtoActual.isEmpty());
        verify(paymentRepository).findById(ID_PARAMETER);
        verify(paymentMapper, never()).toDto(any(Payment.class));
    }
}
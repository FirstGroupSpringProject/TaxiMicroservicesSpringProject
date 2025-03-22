package com.aston.paymentbillingservice.repository;

import com.aston.paymentbillingservice.entity.Payment;
import com.aston.paymentbillingservice.entity.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class PaymentRepositoryTest {

    private static final UUID ID_PARAMETER_NOT_FOUND = UUID.fromString("3f9611f4-f29e-4b4b-bfbf-602f46a36192");
    private static final BigDecimal AMOUNT_PARAMETER = BigDecimal.valueOf(350.6);
    private static final PaymentStatus PAYMENT_STATUS_PARAMETER = PaymentStatus.SUCCEEDED;
    @Autowired
    private PaymentRepository paymentRepository;
    private Payment paymentExpected;

    @BeforeEach
    void setUp() {
        paymentExpected = new Payment();
        paymentExpected.setAmount(AMOUNT_PARAMETER);
        paymentExpected.setStatus(PAYMENT_STATUS_PARAMETER);
    }

    @Test
    public void findById() {
        Payment paymentSaveActual = paymentRepository.save(paymentExpected);

        Payment paymentFoundByIdActual = paymentRepository.findById(paymentSaveActual.getId()).orElse(null);

        assertThat(paymentFoundByIdActual.getId()).isNotNull();
        assertThat(paymentFoundByIdActual.getAmount()).isEqualTo(AMOUNT_PARAMETER);
        assertThat(paymentFoundByIdActual.getStatus()).isEqualTo(PAYMENT_STATUS_PARAMETER);
    }

    @Test
    public void findByIdNotFound() {
        Optional<Payment> paymentFoundByIdActual = paymentRepository.findById(ID_PARAMETER_NOT_FOUND);

        assertThat(paymentFoundByIdActual).isEmpty();
    }

    @Test
    public void save() {
        Payment paymentActual = paymentRepository.save(paymentExpected);

        assertThat(paymentActual.getId()).isNotNull();
        assertThat(paymentActual.getAmount()).isEqualTo(AMOUNT_PARAMETER);
        assertThat(paymentActual.getStatus()).isEqualTo(PAYMENT_STATUS_PARAMETER);
    }

    @Test
    public void delete() {
        Payment paymentSaveActual = paymentRepository.save(paymentExpected);

        paymentRepository.delete(paymentSaveActual);

        Payment paymentFoundByIdActual = paymentRepository.findById(paymentSaveActual.getId()).orElse(null);
        assertThat(paymentFoundByIdActual).isNull();
    }
}
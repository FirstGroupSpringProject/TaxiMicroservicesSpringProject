package com.aston.paymentbillingservice.repository;

import com.aston.paymentbillingservice.entity.Invoice;
import com.aston.paymentbillingservice.entity.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class InvoiceRepositoryTest {

    private static final UUID ID_PARAMETER_NOT_FOUND = UUID.fromString("b8b20b96-3cc2-4c32-95a4-3e1c1e01dd0c");
    private static final UUID ID_USER_PARAMETER = UUID.fromString("3f9611f4-f29e-4b4b-bfbf-602f46a36192");
    private static final PaymentMethod PAYMENT_METHOD_PARAMETER = PaymentMethod.CARD;
    private static final LocalDateTime DUE_DATE_PARAMETER = LocalDateTime.parse("2025-02-14T15:30:40.811420500");
    @Autowired
    private InvoiceRepository invoiceRepository;
    private Invoice invoiceExpected;

    @BeforeEach
    void setUp() {
        invoiceExpected = new Invoice();
        invoiceExpected.setUserId(ID_USER_PARAMETER);
        invoiceExpected.setPaymentMethod(PAYMENT_METHOD_PARAMETER);
        invoiceExpected.setDueDate(DUE_DATE_PARAMETER);
    }

    @Test
    public void findById() {
        Invoice invoiceSaveActual = invoiceRepository.save(invoiceExpected);

        Invoice invoiceFoundByIdActual = invoiceRepository.findById(invoiceSaveActual.getId()).orElse(null);

        assertThat(invoiceFoundByIdActual.getId()).isNotNull();
        assertThat(invoiceFoundByIdActual.getUserId()).isEqualTo(ID_USER_PARAMETER);
        assertThat(invoiceFoundByIdActual.getPaymentMethod()).isEqualTo(PAYMENT_METHOD_PARAMETER);
        assertThat(invoiceFoundByIdActual.getDueDate()).isEqualTo(DUE_DATE_PARAMETER);
    }

    @Test
    public void findByIdNotFound() {
        Optional<Invoice> invoiceFoundByIdActual = invoiceRepository.findById(ID_PARAMETER_NOT_FOUND);

        assertThat(invoiceFoundByIdActual).isEmpty();
    }

    @Test
    public void save() {
        Invoice invoiceActual = invoiceRepository.save(invoiceExpected);

        assertThat(invoiceActual.getId()).isNotNull();
        assertThat(invoiceActual.getUserId()).isEqualTo(ID_USER_PARAMETER);
        assertThat(invoiceActual.getPaymentMethod()).isEqualTo(PAYMENT_METHOD_PARAMETER);
        assertThat(invoiceActual.getDueDate()).isEqualTo(DUE_DATE_PARAMETER);
    }

    @Test
    public void delete() {
        Invoice invoiceSaveActual = invoiceRepository.save(invoiceExpected);

        invoiceRepository.delete(invoiceSaveActual);

        Invoice invoiceFoundByIdActual = invoiceRepository.findById(invoiceSaveActual.getId()).orElse(null);
        assertThat(invoiceFoundByIdActual).isNull();
    }
}
package com.example.currencyinfoapp.exchangerate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateInvoiceRepository
        extends JpaRepository<ExchangeRateInvoiceEntity, Long> {
}

package com.example.currencyinfoapp.exchangerate;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateInvoiceRepository extends JpaRepository<ExchangeRateInvoiceEntity, Long> {
}
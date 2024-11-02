package com.example.currencyinfoapp.exchangerate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateQueryRepository extends JpaRepository<ExchangeRateQueryEntity, Long> {
}

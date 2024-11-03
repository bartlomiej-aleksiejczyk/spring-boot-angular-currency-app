package com.example.currencyinfoapp.exchangerate.dtos;

import java.time.LocalDateTime;

public record ExchangeRateQueryDTO(String currency, String name, LocalDateTime date, double value) {
}

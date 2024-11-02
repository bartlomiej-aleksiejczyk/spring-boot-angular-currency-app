package com.example.currencyinfoapp.exchangerate.dtos;

import java.time.LocalDateTime;

public record ExchangeRateDTO(String currency, String name, LocalDateTime date, double value) {
}

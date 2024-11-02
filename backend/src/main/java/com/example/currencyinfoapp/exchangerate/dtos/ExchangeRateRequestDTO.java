package com.example.currencyinfoapp.exchangerate.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExchangeRateRequestDTO(
        @NotBlank(message = "Currency must not be blank") @Size(max = 255,
                message = "Currency must not exceed 255 characters") String currency,
        @NotBlank(message = "Name must not be blank") @Size(max = 255,
                message = "Name must not exceed 255 characters") String name) {
}

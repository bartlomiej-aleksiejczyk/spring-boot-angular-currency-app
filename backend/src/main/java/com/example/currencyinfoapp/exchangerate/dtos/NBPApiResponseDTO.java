package com.example.currencyinfoapp.exchangerate.dtos;

import java.util.List;

public record NBPApiResponseDTO(String table, String no, String effectiveDate,
                List<NBPApiExchangeRate> rates) {

}

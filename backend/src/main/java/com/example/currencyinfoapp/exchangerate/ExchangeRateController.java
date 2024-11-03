package com.example.currencyinfoapp.exchangerate;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateQueryDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateRequestDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateResponseDTO;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @PostMapping("/get-current-currency-value-command")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRateResponseDTO getCurrentCurrencyValue(
            @Valid @RequestBody ExchangeRateRequestDTO request) {
        return exchangeRateService.getExchangeRate(request);
    }

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ExchangeRateQueryDTO> getAllCurrencyQueries() {
        return exchangeRateService.getAllQueries();
    }
}

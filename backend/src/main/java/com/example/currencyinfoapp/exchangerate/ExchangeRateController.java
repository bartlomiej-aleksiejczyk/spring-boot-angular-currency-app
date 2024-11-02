package com.example.currencyinfoapp.exchangerate;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateDTO;
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
        System.out.println(request);
        return exchangeRateService.getExchangeRate(request);
    }

    @GetMapping("/queries")
    @ResponseStatus(HttpStatus.OK)
    public List<ExchangeRateDTO> getAllCurrencyQueries() {
        return exchangeRateService.getAllQueries();
    }
}

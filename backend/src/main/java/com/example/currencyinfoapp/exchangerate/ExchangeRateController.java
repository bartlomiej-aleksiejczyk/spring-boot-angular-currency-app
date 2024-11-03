package com.example.currencyinfoapp.exchangerate;

import java.util.List;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;

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
    @Operation(summary = "Retrieves the current exchange rate of a specified currency",
            description = "Post a currency code to receive its current exchange rate.",
            responses = {@ApiResponse(responseCode = "200",
                    description = "Successful retrieval of currency exchange rate",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeRateResponseDTO.class),
                            examples = {@ExampleObject(name = "Example",
                                    value = "{\"value\": 1.1234}")})),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters")})
    public ExchangeRateResponseDTO getCurrentCurrencyValue(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Currency code and name of requester user", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeRateRequestDTO.class),
                            examples = {@ExampleObject(name = "Example 1",
                                    value = "{\"currency\": \"USD\", \"name\": \"John Doe\"}"),
                                    @ExampleObject(name = "Example 2",
                                            value = "{\"currency\": \"EUR\", \"name\": \"Jane Doe\"}")})) @Valid @RequestBody ExchangeRateRequestDTO request) {
        return exchangeRateService.getExchangeRate(request);
    }


    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all currency queries made",
            description = "Fetch a list of all currency exchange queries.",
            responses = {@ApiResponse(responseCode = "200", description = "List of all queries",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(
                            schema = @Schema(implementation = ExchangeRateQueryDTO.class))))})
    public List<ExchangeRateQueryDTO> getAllCurrencyQueries() {
        return exchangeRateService.getAllQueries();
    }
}

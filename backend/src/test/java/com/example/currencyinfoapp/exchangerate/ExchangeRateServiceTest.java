package com.example.currencyinfoapp.exchangerate;

import java.time.LocalDateTime;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateQueryDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateRequestDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateResponseDTO;
import com.example.currencyinfoapp.exchangerate.dtos.NBPApiResponseDTO;
import com.example.currencyinfoapp.exchangerate.dtos.NBPApiExchangeRate;

class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateQueryRepository exchangeRateQueryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private NBPApiResponseDTO mockApiResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockApiResponse = new NBPApiResponseDTO("A", "213/A/NBP/2024", "2024-11-3",
                List.of(new NBPApiExchangeRate("dolar amerykański", "USD", 4.0059),
                        new NBPApiExchangeRate("dolar kanadyjski", "CAD", 2.8784),
                        new NBPApiExchangeRate("euro", "EUR", 4.3530),
                        new NBPApiExchangeRate("frank szwajcarski", "CHF", 4.6296),
                        new NBPApiExchangeRate("korona czeska", "CZK", 0.1716),
                        new NBPApiExchangeRate("funt szterling", "GBP", 5.2007),
                        new NBPApiExchangeRate("won południowokoreański", "KRW", 0.002907),
                        new NBPApiExchangeRate("jen (Japonia)", "JPY", 0.026255)));

        when(restTemplate.getForObject(any(String.class), eq(NBPApiResponseDTO[].class)))
                .thenReturn(new NBPApiResponseDTO[] {mockApiResponse});
    }

    @Test
    void testGetExchangeRateUSDSuccess() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("USD", "John Doe");

        ExchangeRateResponseDTO response = exchangeRateService.getExchangeRate(request);

        assertEquals(4.0059, response.value());
    }

    @Test
    void testGetExchangeRateEURSuccess() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("EUR", "Jane Doe");

        ExchangeRateResponseDTO response = exchangeRateService.getExchangeRate(request);

        assertEquals(4.3530, response.value());
    }

    @Test
    void testGetExchangeRateCurrencyNotFound() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("ABC", "John Doe");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getExchangeRate(request);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Exchange rate not found for currency: ABC", exception.getReason());
    }

    @Test
    void testGetExchangeRateUnexpectedApiResponseFormat() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("USD", "John Doe");

        when(restTemplate.getForObject(any(String.class), eq(NBPApiResponseDTO[].class)))
                .thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getExchangeRate(request);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode(),
                "Expected 500 INTERNAL_SERVER_ERROR");
        assertEquals("Internal error occurred", exception.getReason(),
                "Unexpected exception reason");
    }

    @Test
    void testGetExchangeRateServiceUnavailable() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("USD", "John Doe");

        Mockito.when(restTemplate.getForObject(any(String.class), eq(NBPApiResponseDTO[].class)))
                .thenThrow(new RestClientException("Service unavailable"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getExchangeRate(request);
        });

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatusCode());
        assertEquals("External service is temporarily unavailable", exception.getReason());
    }

    @Test
    void testSaveExchangeRateQueryDatabaseAccessException() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("USD", "John Doe");

        doThrow(new DataAccessException("Database error") {}).when(exchangeRateQueryRepository)
                .save(any());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getExchangeRate(request);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Internal error occurred", exception.getReason());
    }

    @Test
    void testGetExchangeRateEmptyApiResponse() {
        ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("USD", "John Doe");

        Mockito.when(restTemplate.getForObject(any(String.class), eq(NBPApiResponseDTO[].class)))
                .thenReturn(new NBPApiResponseDTO[] {});

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getExchangeRate(request);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Internal error occurred", exception.getReason());
    }

    @Test
    void testGetAllQueriesSuccess() {
        List<ExchangeRateQueryEntity> mockQueries = List.of(
                new ExchangeRateQueryEntity((long) 1, "USD", "John Doe", LocalDateTime.now(),
                        4.0059),
                new ExchangeRateQueryEntity((long) 2, "EUR", "Jane Doe", LocalDateTime.now(),
                        4.3530));
        Mockito.when(exchangeRateQueryRepository.findAll()).thenReturn(mockQueries);

        List<ExchangeRateQueryDTO> results = exchangeRateService.getAllQueries();

        assertEquals(2, results.size());
        assertEquals("USD", results.get(0).currency());
        assertEquals("EUR", results.get(1).currency());
    }

    @Test
    void testGetAllQueriesDatabaseAccessException() {
        Mockito.when(exchangeRateQueryRepository.findAll())
                .thenThrow(new DataAccessException("DB Error") {});

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exchangeRateService.getAllQueries();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Internal error occurred", exception.getReason());
    }


}

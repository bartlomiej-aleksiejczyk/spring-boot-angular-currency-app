package com.example.currencyinfoapp.exchangerate;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.currencyinfoapp.common.GlobalExceptionHandler;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateQueryDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateRequestDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateResponseDTO;

@WebMvcTest(ExchangeRateController.class)
@Import({GlobalExceptionHandler.class})
class ExchangeRateControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ExchangeRateService exchangeRateService;

        @InjectMocks
        private ExchangeRateController exchangeRateController;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void testGetCurrentCurrencyValue_Success() throws Exception {
                ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("USD", "John Doe");
                ExchangeRateResponseDTO response = new ExchangeRateResponseDTO(4.0059);
                when(exchangeRateService.getExchangeRate(request)).thenReturn(response);

                mockMvc.perform(post("/currencies/get-current-currency-value-command")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"currency\":\"USD\", \"name\":\"John Doe\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.value").value(4.0059));
        }

        @Test
        void testGetCurrentCurrencyValueValidationError() throws Exception {
                mockMvc.perform(post("/currencies/get-current-currency-value-command")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"John Doe\"}"))

                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.title").value("Bad Request"))
                                .andExpect(jsonPath("$.type").value(
                                                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        }

        @Test
        void testGetCurrentCurrencyValueNotFound() throws Exception {
                ExchangeRateRequestDTO request = new ExchangeRateRequestDTO("ABC", "John Doe");
                when(exchangeRateService.getExchangeRate(request))
                                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Exchange rate not found for currency: ABC"));

                mockMvc.perform(post("/currencies/get-current-currency-value-command")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"currency\":\"ABC\", \"name\":\"John Doe\"}"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.title").value("Not Found"))
                                .andExpect(jsonPath("$.detail").value(
                                                "Exchange rate not found for currency: ABC"));
        }

        @Test
        void testGetAllCurrencyQueriesSuccess() throws Exception {
                List<ExchangeRateQueryDTO> mockResponse = List.of(
                                new ExchangeRateQueryDTO("USD", "John Doe",
                                                LocalDateTime.parse("2024-11-03T12:10:20"), 4.0059),
                                new ExchangeRateQueryDTO("EUR", "Jane Doe",
                                                LocalDateTime.parse("2024-11-03T10:14:35"),
                                                4.3530));

                when(exchangeRateService.getAllQueries()).thenReturn(mockResponse);

                mockMvc.perform(get("/currencies/requests").contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$[0].currency").value("USD"))
                                .andExpect(jsonPath("$[1].currency").value("EUR"));
        }

}

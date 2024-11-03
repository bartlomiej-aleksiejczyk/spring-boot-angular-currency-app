package com.example.currencyinfoapp.exchangerate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateQueryDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateRequestDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateResponseDTO;
import com.example.currencyinfoapp.exchangerate.dtos.NBPApiResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateService {

        private static final String NBP_API_URL =
                        "http://api.nbp.pl/api/exchangerates/tables/A?format=json";
        private final ExchangeRateQueryRepository exchangeRateQueryRepository;
        private final RestTemplate restTemplate;

        public ExchangeRateResponseDTO getExchangeRate(ExchangeRateRequestDTO request) {
                try {
                        NBPApiResponseDTO[] responseArray = restTemplate.getForObject(NBP_API_URL,
                                        NBPApiResponseDTO[].class);

                        if (responseArray == null || responseArray.length == 0) {
                                log.error("Received null or empty response array from NBP API");
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Internal error occurred");
                        }

                        NBPApiResponseDTO response = responseArray[0];
                        if (response == null)
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Internal error occurred");

                        Double exchangeValue = response.rates().stream()
                                        .filter(rate -> request.currency().equals(rate.code()))
                                        .map(rate -> rate.mid()).findFirst()
                                        .orElseThrow(() -> new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Exchange rate not found for currency: "
                                                                        + request.currency()));

                        saveExchangeRateQuery(request, exchangeValue);

                        return new ExchangeRateResponseDTO(exchangeValue);

                } catch (HttpClientErrorException.NotFound e) {
                        log.warn("NPB API responded with status NOT_FOUND", e);
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "External service is temporarily unavailable", e);

                } catch (RestClientException e) {
                        log.error("Error communicating with the NBP API", e);
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                        "External service is temporarily unavailable", e);
                } catch (ResponseStatusException e) {
                        throw e;
                } catch (Exception e) {
                        log.error("Unexpected error occurred while fetching exchange rate", e);
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "An unexpected error occurred", e);
                }
        }

        public List<ExchangeRateQueryDTO> getAllQueries() {
                try {
                        return exchangeRateQueryRepository.findAll().stream()
                                        .map(ExchangeRateQueryEntity::toDTO)
                                        .collect(Collectors.toList());

                } catch (DataAccessException e) {
                        log.error("Database access error while retrieving all queries", e);
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Internal error occurred", e);
                }
        }

        private void saveExchangeRateQuery(ExchangeRateRequestDTO request, Double exchangeValue) {
                ExchangeRateQueryEntity entity = new ExchangeRateQueryEntity();
                entity.setCurrency(request.currency());
                entity.setName(request.name());
                entity.setDate(LocalDateTime.now());
                entity.setExchangeValue(exchangeValue);

                try {
                        exchangeRateQueryRepository.save(entity);
                } catch (DataAccessException e) {
                        log.error("Failed to save exchange rate query for currency: {}",
                                        request.currency(), e);
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Internal error occurred", e);
                }
        }
}

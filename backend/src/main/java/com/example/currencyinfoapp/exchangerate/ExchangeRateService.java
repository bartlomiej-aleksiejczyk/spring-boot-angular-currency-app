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

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateRequestDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateResponseDTO;
import com.example.currencyinfoapp.exchangerate.dtos.NBPApiResponseDTO;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
        private static final String NBP_API_URL =
                        "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

        private final ExchangeRateQueryRepository exchangeRateQueryRepository;

        public ExchangeRateResponseDTO getExchangeRate(ExchangeRateRequestDTO request) {
                RestTemplate restTemplate = new RestTemplate();
                try {
                        NBPApiResponseDTO[] responseArray = restTemplate.getForObject(NBP_API_URL,
                                        NBPApiResponseDTO[].class);
                        NBPApiResponseDTO response =
                                        responseArray != null && responseArray.length > 0
                                                        ? responseArray[0]
                                                        : null;
                        if (response == null)
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Unexpected response format from API");
                        Double value = response.rates().stream()
                                        .filter(rate -> request.currency().equals(rate.code()))
                                        .map(rate -> (Double) rate.mid()).findFirst()
                                        .orElseThrow(() -> new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Currency not found"));

                        ExchangeRateQueryEntity entity = new ExchangeRateQueryEntity();
                        entity.setCurrency(request.currency());
                        entity.setName(request.name());
                        entity.setDate(LocalDateTime.now());
                        entity.setExchangeValue(value);
                        exchangeRateQueryRepository.save(entity);

                        return new ExchangeRateResponseDTO(value);
                } catch (HttpClientErrorException.NotFound e) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Currency not found in the NBP API", e);
                } catch (RestClientException e) {
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                        "External service is temporarily unavailable", e);
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Unexpected error occurred", e);
                }
        }

        public List<ExchangeRateDTO> getAllQueries() {
                try {
                        return exchangeRateQueryRepository.findAll().stream()
                                        .map(ExchangeRateQueryEntity::toDTO)
                                        .collect(Collectors.toList());
                } catch (DataAccessException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Database error occurred", e);
                }
        }

}

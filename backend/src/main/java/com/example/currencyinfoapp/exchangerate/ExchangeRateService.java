package com.example.currencyinfoapp.exchangerate;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateRequestDTO;
import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateResponseDTO;
import com.example.currencyinfoapp.exchangerate.dtos.NBPApiResponseDTO;

@RequiredArgsConstructor
public class ExchangeRateService {
    private static final String NBP_API_URL =
            "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

    private final ExchangeRateQueryRepository exchangeRateQueryRepository;

    public ExchangeRateResponseDTO getCurrencyValue(ExchangeRateRequestDTO request) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            NBPApiResponseDTO response =
                    restTemplate.getForObject(NBP_API_URL, NBPApiResponseDTO.class);
            if (response == null)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Unexpected response format from API");
            Double value =
                    response.rates().stream().filter(rate -> request.currency().equals(rate.code()))
                            .map(rate -> (Double) rate.mid()).findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Currency not found"));

            ExchangeRateQueryEntity entity = new ExchangeRateQueryEntity();
            entity.setCurrency(request.currency());
            entity.setName(request.name());
            entity.setDate(LocalDateTime.now());
            entity.setValue(value);
            exchangeRateQueryRepository.save(entity);

            return new ExchangeRateResponseDTO(value);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error fetching currency data", e);
        }
    }

    public List<ExchangeRateQueryEntity> getAllQuerys() {
        return exchangeRateQueryRepository.findAll();
    }

}

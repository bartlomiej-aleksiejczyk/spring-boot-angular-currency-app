package com.example.currencyinfoapp.exchangerate;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.example.currencyinfoapp.exchangerate.dtos.ExchangeRateQueryDTO;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateQueryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String currency;

    @Column(length = 255)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private double exchangeValue;

    public ExchangeRateQueryDTO toDTO() {
        return new ExchangeRateQueryDTO(currency, name, date, exchangeValue);
    }
}

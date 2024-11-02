package com.example.currencyinfoapp.exchangerate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateInvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String currency;

    @Column(length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private double value;
}

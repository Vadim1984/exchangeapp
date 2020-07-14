package com.example.exchangeapp.dto;

import com.example.exchangeapp.enums.Currency;

import java.math.BigDecimal;

public class ExchangeRateDto {
    private Currency from;
    private Currency to;
    private BigDecimal rate;

    public Currency getFrom() {
        return from;
    }

    public void setFrom(Currency from) {
        this.from = from;
    }

    public Currency getTo() {
        return to;
    }

    public void setTo(Currency to) {
        this.to = to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}

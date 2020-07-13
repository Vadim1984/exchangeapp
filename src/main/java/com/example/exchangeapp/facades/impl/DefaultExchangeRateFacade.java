package com.example.exchangeapp.facades.impl;

import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.dto.ExchangeRequestDto;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.enums.OperationType;
import com.example.exchangeapp.facades.ExchangeRateFacade;
import com.example.exchangeapp.models.CommissionModel;
import com.example.exchangeapp.dto.ExchangeRateDto;
import com.example.exchangeapp.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DefaultExchangeRateFacade implements ExchangeRateFacade {

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public List<ExchangeRateDto> getExchangeRates() {
        final List<PrivatBankExchangeRateDto> exchangeRates = exchangeRateService.getExchangeRates();

        return exchangeRates.stream()
                .map(exchangeRate -> conversionService.convert(exchangeRate, ExchangeRateDto.class))
                .collect(toList());
    }

    @Override
    public void createCommission(CommissionDto commission) {
        exchangeRateService.createCommission(conversionService.convert(commission, CommissionModel.class));
    }

    @Override
    public List<CommissionDto> getAllCommissions() {
        return exchangeRateService.getAllCommissions().stream()
                .map(commission -> conversionService.convert(commission, CommissionDto.class))
                .collect(toList());
    }

    @Override
    public ExchangeRequestDto exchange(ExchangeRequestDto exchangeRequest) {
        Currency currencyFrom = exchangeRequest.getCurrencyFrom();
        Currency currencyTo = exchangeRequest.getCurrencyTo();

        if (OperationType.GIVE.equals(exchangeRequest.getOperationType())) {
            double targetAmount = exchangeRateService.exchange(currencyFrom, currencyTo, exchangeRequest.getAmountFrom());
            exchangeRequest.setAmountTo(targetAmount);
        } else if (OperationType.GET.equals(exchangeRequest.getOperationType())) {
            double targetAmount = exchangeRateService.exchange(currencyTo, currencyFrom, exchangeRequest.getAmountTo());
            exchangeRequest.setAmountFrom(targetAmount);
        }

        return exchangeRequest;
    }

}

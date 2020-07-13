package com.example.exchangeapp.services.impl;

import com.example.exchangeapp.dao.CommissionRepository;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.models.CommissionModel;
import com.example.exchangeapp.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultExchangeRateService implements ExchangeRateService {

    @Value("${privatbank.exchange.url}")
    private String exchangeUrl;

    @Autowired
    private CommissionRepository commisionRepository;

    @Override
    public List<PrivatBankExchangeRateDto> getExchangeRates() {
        RestTemplate restClient = new RestTemplate();
        ResponseEntity<List<PrivatBankExchangeRateDto>> response =
                restClient.exchange(exchangeUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<PrivatBankExchangeRateDto>>() {
                        });
        return response.getBody();
    }

    @Override
    public void createCommission(CommissionModel commission) {
        final CommissionModel existingCommission =
                commisionRepository.findByFromAndTo(commission.getFrom(), commission.getTo());

        if (existingCommission != null) {
            existingCommission.setCommission(commission.getCommission());
            commisionRepository.save(existingCommission);
        } else {
            commisionRepository.save(commission);
        }
    }

    @Override
    public List<CommissionModel> getAllCommissions() {
        final Iterable<CommissionModel> allCommissions = commisionRepository.findAll();

        List<CommissionModel> result = new ArrayList<>();
        for (CommissionModel commission : allCommissions) {
            result.add(commission);
        }

        return result;
    }

    @Override
    public double findCommissionByCurrencyFromAndCurrencyTo(Currency from, Currency to) {
        double commission = 0;
        CommissionModel commissionModel = commisionRepository.findByFromAndTo(from, to);
        if (commissionModel != null) {
            commission = commissionModel.getCommission();
        }
        return commission;
    }

    @Override
    public double exchange(Currency currencyFrom, Currency currencyTo, double amountInCurrencyFrom) {
        double commission = findCommissionByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);

        List<PrivatBankExchangeRateDto> exchangeRates = getExchangeRates();

        double saleCurrencyRate = exchangeRates.stream()
                .filter(rate -> currencyFrom.equals(rate.getCcy()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Currency " + currencyFrom + " is not supported"))
                .getBuy();

        double buyCurrencyRate = exchangeRates.stream()
                .filter(rate -> currencyTo.equals(rate.getCcy()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Currency " + currencyTo + " is not supported"))
                .getSale();

        final double amountInCurrencyTo = amountInCurrencyFrom * saleCurrencyRate / buyCurrencyRate;
        return amountInCurrencyTo - amountInCurrencyTo * commission / 100;
    }

}

package com.example.exchangeapp.services.impl;

import com.example.exchangeapp.dao.CommissionRepository;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.exceptions.ExchangeException;
import com.example.exchangeapp.models.CommissionModel;
import com.example.exchangeapp.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.exchangeapp.constants.ExchangeAppConstants.*;

@Service
public class DefaultExchangeRateService implements ExchangeRateService {

    @Value("${privatbank.exchange.url}")
    private String exchangeUrl;

    @Autowired
    private CommissionRepository commisionRepository;

    @Cacheable("rates")
    @Override
    public List<PrivatBankExchangeRateDto> getExchangeRates() {
        RestTemplate restClient = new RestTemplate();

        try {
            ResponseEntity<List<PrivatBankExchangeRateDto>> response =
                    restClient.exchange(exchangeUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<PrivatBankExchangeRateDto>>() {
                            });

            return response.getBody();
        } catch (RestClientException exception) {
            throw new ExchangeException("currently service is unavailable, please try later.");
        }
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
    public BigDecimal findCommissionByCurrencyFromAndCurrencyTo(Currency from, Currency to) {
        CommissionModel commissionModel = commisionRepository.findByFromAndTo(from, to);

        if (commissionModel != null) {
            return commissionModel.getCommission();
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal calculateGiveOperationAmount(BigDecimal amountFrom, BigDecimal rate, BigDecimal commission){
        BigDecimal targetAmount = amountFrom
                .multiply(rate);

        BigDecimal totalCommission = targetAmount
                .multiply(commission)
                .divide(ONE_HUNDRED, RATES_SCALE, ROUNDING_MODE);

        return targetAmount
                .subtract(totalCommission)
                .setScale(AMOUNT_SCALE, ROUNDING_MODE);
    }

    public BigDecimal calculateGetOperationAmount(BigDecimal amountTo, BigDecimal rate, BigDecimal commission){

        return amountTo
                .multiply(ONE_HUNDRED)
                .divide(rate.multiply(ONE_HUNDRED)
                                .subtract(rate.multiply(commission))
                        ,AMOUNT_SCALE, ROUNDING_MODE
                );
    }

}

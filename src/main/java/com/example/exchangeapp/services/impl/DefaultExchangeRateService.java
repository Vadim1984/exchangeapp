package com.example.exchangeapp.services.impl;

import com.example.exchangeapp.dao.CommissionRepository;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.exceptions.PrivatBankApiUnavailableException;
import com.example.exchangeapp.models.CommissionModel;
import com.example.exchangeapp.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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

        try {
            ResponseEntity<List<PrivatBankExchangeRateDto>> response =
                    restClient.exchange(exchangeUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<PrivatBankExchangeRateDto>>() {
                            });

            return response.getBody();
        } catch (RestClientException exception) {
            throw new PrivatBankApiUnavailableException(exception);
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
}

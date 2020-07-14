package com.example.exchangeapp.facades.impl;

import com.example.exchangeapp.constants.ExchangeAppConstants;
import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.dto.ExchangeRateDto;
import com.example.exchangeapp.dto.ExchangeRequestDto;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.enums.OperationType;
import com.example.exchangeapp.facades.ExchangeRateFacade;
import com.example.exchangeapp.models.CommissionModel;
import com.example.exchangeapp.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DefaultExchangeRateFacade implements ExchangeRateFacade {

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private GenericConversionService genericConversionService;
    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public List<ExchangeRateDto> getExchangeRates() {
        final List<PrivatBankExchangeRateDto> exchangeRates = exchangeRateService.getExchangeRates();
        final TypeDescriptor sourceTypeDescriptor = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PrivatBankExchangeRateDto.class));
        final TypeDescriptor targetTypeDescriptor = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ExchangeRateDto.class));

        return (List<ExchangeRateDto>)conversionService.convert(exchangeRates, sourceTypeDescriptor, targetTypeDescriptor);
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

    @CacheEvict(cacheNames="rates", allEntries=true)
    @Override
    public ExchangeRequestDto exchange(ExchangeRequestDto exchangeRequest) {
        List<ExchangeRateDto> exchangeRates = getExchangeRates();
        Currency currencyFrom = exchangeRequest.getCurrencyFrom();
        Currency currencyTo = exchangeRequest.getCurrencyTo();
        final IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
                "Pair of currencies: " + currencyFrom + ", " + currencyTo + " are not supported");

        if (OperationType.GIVE.equals(exchangeRequest.getOperationType())) {
            ExchangeRateDto exchangeRateDto = exchangeRates.stream()
                    .filter(rate -> rate.getFrom().equals(currencyFrom))
                    .filter(rate -> rate.getTo().equals(currencyTo))
                    .findFirst()
                    .orElseThrow(() -> illegalArgumentException);

            BigDecimal targetAmount = exchangeRequest.getAmountFrom().multiply(exchangeRateDto.getRate()).setScale(
                    ExchangeAppConstants.AMOUNT_SCALE, ExchangeAppConstants.ROUNDING_MODE);
            exchangeRequest.setAmountTo(targetAmount);
        } else if (OperationType.GET.equals(exchangeRequest.getOperationType())) {
            ExchangeRateDto exchangeRateDto = exchangeRates.stream()
                    .filter(rate -> rate.getFrom().equals(currencyTo))
                    .filter(rate -> rate.getTo().equals(currencyFrom))
                    .findFirst()
                    .orElseThrow(() -> illegalArgumentException);
            BigDecimal targetAmount = exchangeRequest.getAmountTo().multiply(exchangeRateDto.getRate()).setScale(
                    ExchangeAppConstants.AMOUNT_SCALE, ExchangeAppConstants.ROUNDING_MODE);
            exchangeRequest.setAmountFrom(targetAmount);
        }

        return exchangeRequest;
    }
}

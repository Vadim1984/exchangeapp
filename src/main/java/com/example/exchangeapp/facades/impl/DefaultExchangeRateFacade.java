package com.example.exchangeapp.facades.impl;

import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.dto.ExchangeRateDto;
import com.example.exchangeapp.dto.ExchangeRequestDto;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.enums.OperationType;
import com.example.exchangeapp.exceptions.ExchangeException;
import com.example.exchangeapp.facades.ExchangeRateFacade;
import com.example.exchangeapp.models.CommissionModel;
import com.example.exchangeapp.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.example.exchangeapp.constants.ExchangeAppConstants.*;
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

    @CacheEvict(cacheNames = "rates", allEntries = true)
    @Override
    public ExchangeRequestDto exchange(ExchangeRequestDto exchangeRequest) {
        List<ExchangeRateDto> exchangeRates = getExchangeRates();
        Currency currencyFrom = exchangeRequest.getCurrencyFrom();
        Currency currencyTo = exchangeRequest.getCurrencyTo();
        BigDecimal commission = exchangeRateService.findCommissionByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo)
                .setScale(AMOUNT_SCALE, ROUNDING_MODE);
        ExchangeRateDto exchangeRateDto = filterRate(exchangeRates, currencyFrom, currencyTo);

        if (OperationType.GIVE.equals(exchangeRequest.getOperationType())) {
            BigDecimal total = exchangeRateService.calculateGiveOperationAmount(exchangeRequest.getAmountFrom(), exchangeRateDto.getRate(), commission);
            exchangeRequest.setAmountTo(total);
        } else if (OperationType.GET.equals(exchangeRequest.getOperationType())) {
            BigDecimal total = exchangeRateService.calculateGetOperationAmount(exchangeRequest.getAmountTo(), exchangeRateDto.getRate(), commission);
            exchangeRequest.setAmountFrom(total);
        }

        return exchangeRequest;
    }

    private ExchangeRateDto filterRate(List<ExchangeRateDto> exchangeRates, Currency currencyFrom, Currency currencyTo) {
        ExchangeException exchangeException = new ExchangeException(
                "Pair of currencies: " + currencyFrom + ", " + currencyTo + " are not supported");

        return exchangeRates.stream()
                .filter(rate -> rate.getFrom().equals(currencyFrom))
                .filter(rate -> rate.getTo().equals(currencyTo))
                .findFirst()
                .orElseThrow(() -> exchangeException);
    }

}

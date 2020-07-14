package com.example.exchangeapp.converters;

import com.example.exchangeapp.constants.ExchangeAppConstants;
import com.example.exchangeapp.dto.ExchangeRateDto;
import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrivatBankExchangeRateDtoToExchangeRateDtoConverter
        implements Converter<List<PrivatBankExchangeRateDto>, List<ExchangeRateDto>> {

    @Override
    public List<ExchangeRateDto> convert(List<PrivatBankExchangeRateDto> source) {

        List<ExchangeRateDto> target = new ArrayList<>();

        List<PrivatBankExchangeRateDto> privatBankRates = source.stream()
                .filter(rate -> !Currency.BTC.equals(rate.getCcy()))
                .collect(Collectors.toList());

        for (int i = 0; i < privatBankRates.size(); i++) {
            for (int j = i + 1; j < privatBankRates.size(); j++) {
                final PrivatBankExchangeRateDto exchangeRateForeignCurrencyFrom = privatBankRates.get(i);
                final PrivatBankExchangeRateDto exchangeRateForeignCurrencyTo = privatBankRates.get(j);

                ExchangeRateDto exchangeRateDtoGive = new ExchangeRateDto();
                exchangeRateDtoGive.setFrom(exchangeRateForeignCurrencyFrom.getCcy());
                exchangeRateDtoGive.setTo(exchangeRateForeignCurrencyTo.getCcy());
                exchangeRateDtoGive
                        .setRate(calculateExchangeRate(exchangeRateForeignCurrencyFrom, exchangeRateForeignCurrencyTo));

                ExchangeRateDto exchangeRateDtoGet = new ExchangeRateDto();
                exchangeRateDtoGet.setFrom(exchangeRateForeignCurrencyTo.getCcy());
                exchangeRateDtoGet.setTo(exchangeRateForeignCurrencyFrom.getCcy());
                exchangeRateDtoGet
                        .setRate(calculateExchangeRate(exchangeRateForeignCurrencyTo, exchangeRateForeignCurrencyFrom));

                target.add(exchangeRateDtoGive);
                target.add(exchangeRateDtoGet);
            }

            final PrivatBankExchangeRateDto exchangeRateNationalCurrencyFrom = privatBankRates.get(i);
            ExchangeRateDto exchangeRateDtoNationalCurrencyFrom = new ExchangeRateDto();
            exchangeRateDtoNationalCurrencyFrom.setFrom(exchangeRateNationalCurrencyFrom.getCcy());
            exchangeRateDtoNationalCurrencyFrom.setTo(exchangeRateNationalCurrencyFrom.getBase_ccy());
            exchangeRateDtoNationalCurrencyFrom
                    .setRate(exchangeRateNationalCurrencyFrom.getBuy().setScale(ExchangeAppConstants.RATES_SCALE,
                            ExchangeAppConstants.ROUNDING_MODE));

            final PrivatBankExchangeRateDto exchangeRateNationalCurrencyTo = privatBankRates.get(i);
            ExchangeRateDto exchangeRateDtoNationalCurrencyTo = new ExchangeRateDto();
            exchangeRateDtoNationalCurrencyTo.setFrom(exchangeRateNationalCurrencyTo.getBase_ccy());
            exchangeRateDtoNationalCurrencyTo.setTo(exchangeRateNationalCurrencyTo.getCcy());
            exchangeRateDtoNationalCurrencyTo.setRate(BigDecimal.ONE
                    .divide(exchangeRateNationalCurrencyTo.getSale(), ExchangeAppConstants.RATES_SCALE,
                            ExchangeAppConstants.ROUNDING_MODE));

            target.add(exchangeRateDtoNationalCurrencyFrom);
            target.add(exchangeRateDtoNationalCurrencyTo);
        }

        return target;
    }

    static BigDecimal calculateExchangeRate(PrivatBankExchangeRateDto exchangeRateFrom,
                                            PrivatBankExchangeRateDto exchangeRateTo) {
        BigDecimal saleCurrencyRate = exchangeRateFrom.getBuy();
        BigDecimal buyCurrencyRate = exchangeRateTo.getSale();

        return saleCurrencyRate.divide(buyCurrencyRate, ExchangeAppConstants.RATES_SCALE, ExchangeAppConstants.ROUNDING_MODE);
    }
}

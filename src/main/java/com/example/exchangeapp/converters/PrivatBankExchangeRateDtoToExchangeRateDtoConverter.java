package com.example.exchangeapp.converters;

import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.dto.ExchangeRateDto;
import org.springframework.core.convert.converter.Converter;

public class PrivatBankExchangeRateDtoToExchangeRateDtoConverter implements Converter<PrivatBankExchangeRateDto, ExchangeRateDto> {
    @Override
    public ExchangeRateDto convert(PrivatBankExchangeRateDto source) {
        ExchangeRateDto target = new ExchangeRateDto();
        target.setFrom(source.getCcy());
        target.setTo(source.getBase_ccy());
        target.setBuyRate(source.getBuy());
        target.setSaleRate(source.getSale());
        return target;
    }
}

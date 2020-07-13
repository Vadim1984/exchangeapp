package com.example.exchangeapp.config;

import com.example.exchangeapp.converters.CommissionDtoToCommissionModelConverter;
import com.example.exchangeapp.converters.CommissionModelToCommissionDtoConverter;
import com.example.exchangeapp.converters.PrivatBankExchangeRateDtoToExchangeRateDtoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PrivatBankExchangeRateDtoToExchangeRateDtoConverter());
        registry.addConverter(new CommissionDtoToCommissionModelConverter());
        registry.addConverter(new CommissionModelToCommissionDtoConverter());
    }
}

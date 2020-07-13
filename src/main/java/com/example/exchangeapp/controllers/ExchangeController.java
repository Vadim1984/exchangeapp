package com.example.exchangeapp.controllers;

import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.dto.ExchangeRequestDto;
import com.example.exchangeapp.facades.ExchangeRateFacade;
import com.example.exchangeapp.dto.ExchangeRateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeController {

    @Autowired
    private ExchangeRateFacade exchangeRateFacade;

    @GetMapping("/commissions")
    public List<CommissionDto> getAllCommissions(){
        return exchangeRateFacade.getAllCommissions();
    }

    @PostMapping("/commissions")
    public void createCommission(@Valid @RequestBody CommissionDto commission){
        exchangeRateFacade.createCommission(commission);
    }

    @GetMapping("/exchange-rates")
    public List<ExchangeRateDto> getExchangeRates(){
        return exchangeRateFacade.getExchangeRates();
    }

    @PostMapping("/exchange")
    public ExchangeRequestDto exchange(@RequestBody ExchangeRequestDto exchangeRequest){
        return exchangeRateFacade.exchange(exchangeRequest);
    }
}

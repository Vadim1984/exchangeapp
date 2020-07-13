package com.example.exchangeapp.facades;

import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.dto.ExchangeRateDto;
import com.example.exchangeapp.dto.ExchangeRequestDto;

import java.util.List;

public interface ExchangeRateFacade {
    /**
     * Find PrivatBank exchange rates for currency pairs:
     *
     * EUR=UAH,
     * USD=UAH,
     * RUR=UAH,
     * BTC=UAH
     *
     * @return list dto which contains currency pair and sale, buy rates.
     */
    List<ExchangeRateDto> getExchangeRates();

    /**
     * Create commission for currency pair
     *
     * @param commission model
     */
    void createCommission(CommissionDto commission);

    /**
     * Fetch all existing commissions in DB.
     *
     * @return list of commissions.
     */
    List<CommissionDto> getAllCommissions();

    /**
     * Perform conversion amount from one currency into another currency:
     *
     * if(exchangeRequest.operationType = GIVE){
     *     convert exchangeRequest.amountFrom into exchangeRequest.amountTo
     * }
     *
     * if(exchangeRequest.operationType = GET){
     *     convert exchangeRequest.amountTo into exchangeRequest.amountFrom
     * }
     *
     * @param exchangeRequest  - request dto.
     * @return - populated request dto.
     */
    ExchangeRequestDto exchange(ExchangeRequestDto exchangeRequest);
}

package com.example.exchangeapp.services;

import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.models.CommissionModel;

import java.util.List;

public interface ExchangeRateService {
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
    List<PrivatBankExchangeRateDto> getExchangeRates();

    /**
     * Create commission for currency pair
     *
     * @param commission model
     */
    void createCommission(CommissionModel commission);

    /**
     * Fetch all existing commissions in DB.
     *
     * @return list of commissions.
     */
    List<CommissionModel> getAllCommissions();

    /**
     * @param from currency
     * @param to currency
     * @return commission double value
     */
    double findCommissionByCurrencyFromAndCurrencyTo(Currency from, Currency to);

    /**
     * Perform conversion amount from one currency into another currency
     *
     * @param currencyFrom  - source currency which need to be converted.
     * @param currencyTo - target currency which need to be converted.
     * @param amountInCurrencyFrom - source currency amount.
     * @return - target currency amount.
     */
    double exchange(Currency currencyFrom, Currency currencyTo, double amountInCurrencyFrom);
}

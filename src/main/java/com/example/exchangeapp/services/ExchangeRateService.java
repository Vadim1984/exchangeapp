package com.example.exchangeapp.services;

import com.example.exchangeapp.dto.privatbank.PrivatBankExchangeRateDto;
import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.models.CommissionModel;

import java.math.BigDecimal;
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
     * @return commission BigDecimal value
     */
    BigDecimal findCommissionByCurrencyFromAndCurrencyTo(Currency from, Currency to);
}

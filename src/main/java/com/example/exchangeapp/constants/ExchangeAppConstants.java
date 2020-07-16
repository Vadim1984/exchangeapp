package com.example.exchangeapp.constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeAppConstants {
    private ExchangeAppConstants(){

    }

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_DOWN;
    public static final int RATES_SCALE = 4;
    public static final int AMOUNT_SCALE = 2;
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
}

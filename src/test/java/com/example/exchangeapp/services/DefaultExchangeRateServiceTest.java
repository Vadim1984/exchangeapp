package com.example.exchangeapp.services;

import com.example.exchangeapp.dao.CommissionRepository;
import com.example.exchangeapp.services.impl.DefaultExchangeRateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExchangeRateServiceTest {

    @InjectMocks
    private DefaultExchangeRateService testInstance;
    @Mock
    private CommissionRepository commisionRepository;

    @Before
    public void setUp() {

    }

    @Test
    public void testShouldCalculateGiveOperationAmount() {
        final BigDecimal amountFrom = new BigDecimal("10");
        final BigDecimal rate = new BigDecimal("1.1139");
        final BigDecimal commission = new BigDecimal("10");
        final BigDecimal expectedAmountTo = new BigDecimal("10.03");

        final BigDecimal actualAmountTo = testInstance.calculateGiveOperationAmount(amountFrom, rate, commission);

        assertThat(actualAmountTo).isEqualTo(expectedAmountTo);
    }

    @Test
    public void testShouldCalculateGetOperationAmount() {
        final BigDecimal amountTo = new BigDecimal("10.03");
        final BigDecimal rate = new BigDecimal("1.1139");
        final BigDecimal commission = new BigDecimal("10");
        final BigDecimal expectedAmountFrom = new BigDecimal("10.00");

        final BigDecimal actualAmountFrom = testInstance.calculateGetOperationAmount(amountTo, rate, commission);

        assertThat(actualAmountFrom).isEqualTo(expectedAmountFrom);
    }

}

package com.example.exchangeapp.dao;

import com.example.exchangeapp.enums.Currency;
import com.example.exchangeapp.models.CommissionModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CommissionRepository extends CrudRepository<CommissionModel, Integer> {
    /**
     * Find CommissionModel by currency from and currency to.
     *
     * @param from currency.
     * @param to currency.
     * @return CommissionModel.
     */
    CommissionModel findByFromAndTo(Currency from, Currency to);
}

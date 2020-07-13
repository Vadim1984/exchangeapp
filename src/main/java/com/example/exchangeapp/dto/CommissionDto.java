package com.example.exchangeapp.dto;

import com.example.exchangeapp.enums.Currency;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class CommissionDto {

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private double commissionPt;
    @NotNull
    private Currency from;
    @NotNull
    private Currency to;

    public double getCommissionPt() {
        return commissionPt;
    }

    public void setCommissionPt(double commissionPt) {
        this.commissionPt = commissionPt;
    }

    public Currency getFrom() {
        return from;
    }

    public void setFrom(Currency from) {
        this.from = from;
    }

    public Currency getTo() {
        return to;
    }

    public void setTo(Currency to) {
        this.to = to;
    }
}

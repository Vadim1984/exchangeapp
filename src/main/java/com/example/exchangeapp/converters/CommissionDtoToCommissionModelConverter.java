package com.example.exchangeapp.converters;

import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.models.CommissionModel;
import org.springframework.core.convert.converter.Converter;

public class CommissionDtoToCommissionModelConverter implements Converter<CommissionDto, CommissionModel> {

    @Override
    public CommissionModel convert(CommissionDto source) {
        CommissionModel target = new CommissionModel();
        target.setCommission(source.getCommissionPt());
        target.setFrom(source.getFrom());
        target.setTo(source.getTo());

        return target;
    }
}

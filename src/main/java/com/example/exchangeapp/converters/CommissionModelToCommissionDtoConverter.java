package com.example.exchangeapp.converters;

import com.example.exchangeapp.dto.CommissionDto;
import com.example.exchangeapp.models.CommissionModel;
import org.springframework.core.convert.converter.Converter;

public class CommissionModelToCommissionDtoConverter implements Converter<CommissionModel, CommissionDto> {
    @Override
    public CommissionDto convert(CommissionModel source) {
        CommissionDto target = new CommissionDto();
        target.setCommissionPt(source.getCommission());
        target.setFrom(source.getFrom());
        target.setTo(source.getTo());
        return target;
    }
}

package com.example.financingagg.integration.mapper;

import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.dto.fastbank.FastBankApplicationRequestDto;
import org.springframework.stereotype.Component;

@Component
public class FastBankMapper implements OfferMapper<FastBankApplicationRequestDto> {

    @Override
    public FastBankApplicationRequestDto toApplicationDto(OfferRequest requestDto) {
        return FastBankApplicationRequestDto.builder()
                .phoneNumber(requestDto.getPhone())
                .email(requestDto.getEmail())
                .monthlyIncomeAmount(requestDto.getMonthlyIncome())
                .monthlyCreditLiabilities(requestDto.getMonthlyCreditLiabilities())
                .dependents(requestDto.getDependents())
                .agreeToDataSharing(requestDto.isUserAcceptsTos())
                .amount(requestDto.getAmount())
                .build();
    }
}

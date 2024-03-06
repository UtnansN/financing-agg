package com.example.financingagg.integration.mapper.fastbank;

import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.dto.fastbank.FastBankApplicationRequestDto;
import com.example.financingagg.integration.mapper.OfferMapper;
import org.springframework.stereotype.Component;

@Component
public class FastBankMapper implements OfferMapper<FastBankApplicationRequestDto> {

    @Override
    public FastBankApplicationRequestDto toApplicationRequestDto(OfferRequest requestDto) {
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

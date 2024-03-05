package com.example.financingagg.integration.mapper;

import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.dto.solidbank.SolidBankApplicationRequestDto;
import org.springframework.stereotype.Component;

@Component
public class SolidBankMapper implements OfferMapper<SolidBankApplicationRequestDto> {

    @Override
    public SolidBankApplicationRequestDto toApplicationDto(OfferRequest requestDto) {
        return SolidBankApplicationRequestDto.builder()
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .monthlyIncome(requestDto.getMonthlyIncome())
                .monthlyExpenses(requestDto.getMonthlyExpenses())
                .maritalStatus(requestDto.getMaritalStatus())
                .agreeToBeScored(requestDto.isUserAcceptsTos())
                .amount(requestDto.getAmount())
                .build();
    }
}

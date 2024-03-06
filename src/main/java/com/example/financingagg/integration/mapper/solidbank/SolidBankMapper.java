package com.example.financingagg.integration.mapper.solidbank;

import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.dto.solidbank.SolidBankApplicationRequestDto;
import com.example.financingagg.integration.mapper.OfferMapper;
import org.springframework.stereotype.Component;

// Can use mapstruct here.
@Component
public class SolidBankMapper implements OfferMapper<SolidBankApplicationRequestDto> {

    @Override
    public SolidBankApplicationRequestDto toApplicationRequestDto(OfferRequest requestDto) {
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

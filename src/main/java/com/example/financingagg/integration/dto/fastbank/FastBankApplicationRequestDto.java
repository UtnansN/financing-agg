package com.example.financingagg.integration.dto.fastbank;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FastBankApplicationRequestDto {

    @Pattern(regexp = "\\+371[0-9]{8}")
    private String phoneNumber;

    @Email
    private String email;

    @PositiveOrZero
    private BigDecimal monthlyIncomeAmount;

    @PositiveOrZero
    private BigDecimal monthlyCreditLiabilities;

    @PositiveOrZero
    private int dependents;

    private boolean agreeToDataSharing;

    @PositiveOrZero
    private BigDecimal amount;

}

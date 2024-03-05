package com.example.financingagg.integration.dto.fastbank;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    private BigDecimal monthlyIncomeAmount;

    private BigDecimal monthlyCreditLiabilities;

    private int dependents;

    private boolean agreeToDataSharing;

    private BigDecimal amount;

}

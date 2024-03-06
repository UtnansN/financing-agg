package com.example.financingagg.integration.dto.solidbank;

import com.example.financingagg.dto.enums.MaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SolidBankApplicationRequestDto {

    @Pattern(regexp = "\\+[0-9]{11,15}")
    private String phone;

    @Email
    private String email;

    @PositiveOrZero
    private BigDecimal monthlyIncome;

    @PositiveOrZero
    private BigDecimal monthlyExpenses;

    private MaritalStatus maritalStatus;

    private boolean agreeToBeScored;

    @PositiveOrZero
    private BigDecimal amount;

}

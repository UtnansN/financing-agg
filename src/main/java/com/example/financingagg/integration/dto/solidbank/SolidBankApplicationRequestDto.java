package com.example.financingagg.integration.dto.solidbank;

import com.example.financingagg.dto.enums.MaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    private BigDecimal monthlyIncome;

    private BigDecimal monthlyExpenses;

    private MaritalStatus maritalStatus;

    private boolean agreeToBeScored;

    private BigDecimal amount;

}

package com.example.financingagg.dto;

import com.example.financingagg.dto.enums.MaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferRequest {

    @Pattern(regexp = "\\+371[0-9]{8}")
    private String phone;

    @Email
    private String email;

    @PositiveOrZero
    private BigDecimal monthlyIncome;

    @PositiveOrZero
    private BigDecimal monthlyExpenses;

    @PositiveOrZero
    private BigDecimal monthlyCreditLiabilities;

    private MaritalStatus maritalStatus;

    @PositiveOrZero
    private int dependents;

    private boolean userAcceptsTos;

    @PositiveOrZero
    private BigDecimal amount;

}

package com.example.financingagg.dto;

import com.example.financingagg.dto.enums.MaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferRequest {

    @Pattern(regexp = "\\+371[0-9]{8}")
    private String phone;

    @Email
    private String email;

    private BigDecimal monthlyIncome;

    private BigDecimal monthlyExpenses;

    private BigDecimal monthlyCreditLiabilities;

    private MaritalStatus maritalStatus;

    private int dependents;

    private boolean userAcceptsTos;

    private BigDecimal amount;

}

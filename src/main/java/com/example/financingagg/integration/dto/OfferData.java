package com.example.financingagg.integration.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * In reality different companies may require different DTOs to get the same information.
 * In this case these would be defined in their respective packages and mappers would be used to translate to our Offer.
 * Leaving it as a common thing here due to convenience.
 */
@Data
@Builder
public class OfferData {
    private BigDecimal monthlyPaymentAmount;

    private BigDecimal totalRepaymentAmount;

    private int numberOfPayments;

    private BigDecimal annualPercentageRate;

    private LocalDate firstRepaymentDate;
}

package com.example.financingagg.dto;

import com.example.financingagg.integration.dto.OfferData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyOffer {
    // To identify financing company.
    private String company;

    // Some message in case offer is not present.
    private String message;

    // The offer provided by the company. Absence of this would mean that the company was unable to provide an offer.
    private OfferData offerData;

    public static CompanyOffer success(String company, OfferData offerData) {
        return CompanyOffer.builder()
                .company(company)
                .offerData(offerData)
                .build();
    }

    public static CompanyOffer fail(String company, String message) {
        return CompanyOffer.builder()
                .company(company)
                .message(message)
                .build();
    }
}

package com.example.financingagg.integration.provider;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.dto.OfferData;

public interface OfferProvider {
    CompanyOffer getOffer(OfferRequest requestDto);
}

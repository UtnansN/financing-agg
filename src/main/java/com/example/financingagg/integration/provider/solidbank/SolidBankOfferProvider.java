package com.example.financingagg.integration.provider.solidbank;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.dto.OfferData;
import com.example.financingagg.integration.provider.OfferProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SolidBankOfferProvider implements OfferProvider {

    private static final String NAME = "SolidBank";

    @Override
    public CompanyOffer getOffer(OfferRequest requestDto) {
        log.info("Publishing request for Solid Bank");
        try {
            Thread.sleep(7000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Done publishing request for Solid Bank");

        return CompanyOffer.success(NAME, null);
    }
}

package com.example.financingagg.integration.provider.solidbank;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.mapper.solidbank.SolidBankMapper;
import com.example.financingagg.integration.provider.ApplicationsService;
import com.example.financingagg.integration.provider.OfferProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolidBankOfferProvider implements OfferProvider {

    private static final String PROVIDER_ID = "SolidBank";

    @Value("${integrations.solid-bank.applications-url}")
    private String applicationsUrl;

    private final SolidBankMapper solidBankMapper;
    private final ApplicationsService applicationsService;

    @Override
    public CompanyOffer getOffer(OfferRequest requestDto) {
        log.info("Received request for Solid Bank");

        // Map to client dto
        var createApplicationDto = solidBankMapper.toApplicationRequestDto(requestDto);

        // Create an application with client and get the ID
        var applicationId = applicationsService.createApplication(applicationsUrl, createApplicationDto);

        // Poll application until it is not in draft state
        var offer = applicationsService.getFinalizedOffer(applicationsUrl, PROVIDER_ID, applicationId);

        log.info("Finalized request for Solid Bank");
        return offer;
    }
}

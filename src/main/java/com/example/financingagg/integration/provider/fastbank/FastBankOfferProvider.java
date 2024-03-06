package com.example.financingagg.integration.provider.fastbank;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.mapper.fastbank.FastBankMapper;
import com.example.financingagg.integration.provider.ApplicationsService;
import com.example.financingagg.integration.provider.OfferProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FastBankOfferProvider implements OfferProvider {
    private static final String PROVIDER_ID = "FastBank";

    @Value("${integrations.fast-bank.applications-url}")
    private String applicationsUrl;

    private final FastBankMapper fastBankMapper;
    private final ApplicationsService applicationsService;

    @Override
    public CompanyOffer getOffer(OfferRequest requestDto) {
        log.info("Received request for Fast Bank");

        // Map to client dto
        var createApplicationDto = fastBankMapper.toApplicationRequestDto(requestDto);

        // Create an application with client and get the ID
        var applicationId = applicationsService.createApplication(applicationsUrl, createApplicationDto);

        // Poll application until it is not in draft state
        var offer = applicationsService.getFinalizedOffer(applicationsUrl, PROVIDER_ID, applicationId);

        log.info("Finalized request for Fast Bank");
        return offer;
    }
}

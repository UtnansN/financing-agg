package com.example.financingagg.integration.provider.fastbank;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.exception.WaitingException;
import com.example.financingagg.integration.exception.NoOfferException;
import com.example.financingagg.integration.dto.OfferData;
import com.example.financingagg.integration.mapper.FastBankMapper;
import com.example.financingagg.integration.provider.OfferProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FastBankOfferProvider implements OfferProvider {

    private static final String NAME = "FastBank";

    private final FastBankMapper fastBankMapper;
    private final ApplicationsService applicationsService;

    @Override
    public CompanyOffer getOffer(OfferRequest requestDto) {
        log.info("Received request for Fast Bank");

        // Map to client dto
        var createApplicationDto = fastBankMapper.toApplicationDto(requestDto);

        // Create an application with client and get its id
        var applicationId = applicationsService.createApplication(createApplicationDto);

        // Poll application until it is not in draft state
        final OfferData offerDto;
        try {
            offerDto = applicationsService.getFinalizedOffer(applicationId);
        } catch (NoOfferException e) {
            log.info("No offer received for Fast Bank");
            return CompanyOffer.fail(NAME, e.getMessage());
        } catch (WaitingException e) {
            log.info("Company was unable to provide offer in time");
            return CompanyOffer.fail(NAME, e.getMessage());
        }
        log.info("Finalized request for Fast Bank");
        return CompanyOffer.success(NAME, offerDto);
    }
}

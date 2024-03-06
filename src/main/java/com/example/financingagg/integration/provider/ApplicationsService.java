package com.example.financingagg.integration.provider;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.integration.dto.Application;
import com.example.financingagg.integration.dto.ApplicationStatus;
import com.example.financingagg.integration.exception.WaitingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Have the liberty of defining a common ApplicationsService in this case, as the way of accessing the data is the same for both.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationsService {

    private final RestTemplate restTemplate;

    public UUID createApplication(String url, Object createApplicationDto) {
        var postResponse = restTemplate.postForEntity(url, createApplicationDto, Application.class);
        if (postResponse.getStatusCode() != HttpStatus.CREATED || !postResponse.hasBody()) {
            return null;
        }
        return postResponse.getBody().getId();
    }

    @Retryable(retryFor = WaitingException.class,
            maxAttemptsExpression = "${retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${retry.max-delay}"))
    public CompanyOffer getFinalizedOffer(String applicationsUrl, String companyId, UUID applicationId) throws WaitingException {
        if (applicationId == null) {
            return CompanyOffer.fail(companyId, "There was an issue applying for the offer.");
        }

        var applicationDto = restTemplate.getForEntity("%s/%s".formatted(applicationsUrl, applicationId), Application.class);
        var body = applicationDto.getBody();

        if (applicationDto.getStatusCode() != HttpStatus.OK) {
            log.error("There was an issue accessing the application with id: {}", companyId);
            return CompanyOffer.fail(companyId, "There was an issue applying for the offer.");
        }

        var applicationStatus = body.getStatus();
        if (applicationStatus == ApplicationStatus.DRAFT) {
            log.info("Application for company {} is still in DRAFT state - retrying", companyId);
            throw new WaitingException(companyId);
        }

        var offer = body.getOffer();
        if (applicationStatus == ApplicationStatus.PROCESSED && offer != null) {
            log.info("Successfully received an offer from company {}", companyId);
            return CompanyOffer.success(companyId, offer);
        }

        var message = "Company %s was unable to provide an offer".formatted(companyId);
        log.info(message);
        return CompanyOffer.fail(companyId, message);
    }

    @Recover
    private CompanyOffer recover(WaitingException ex) throws WaitingException {
        log.info("Was not able to retrieve offer within given time restraints.");
        return CompanyOffer.fail(ex.getCompanyId(), "Offer request to company %s timed out".formatted(ex.getCompanyId()));
    }
}

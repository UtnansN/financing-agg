package com.example.financingagg.integration.provider.fastbank;

import com.example.financingagg.integration.exception.NoOfferException;
import com.example.financingagg.integration.exception.WaitingException;
import com.example.financingagg.integration.dto.Application;
import com.example.financingagg.integration.dto.ApplicationStatus;
import com.example.financingagg.integration.dto.OfferData;
import com.example.financingagg.integration.dto.fastbank.FastBankApplicationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class ApplicationsService {

    @Value("${integrations.fast-bank.applications-url}")
    private String applicationsUrl;

    private final RestTemplate restTemplate;

    public UUID createApplication(FastBankApplicationRequestDto requestDto) {
        var postResponse = restTemplate.postForEntity(applicationsUrl, requestDto, Application.class);
        if (!postResponse.hasBody()) {
            return null;
        }
        return postResponse.getBody().getId();
    }

    @Retryable(retryFor = WaitingException.class, notRecoverable = NoOfferException.class,
            maxAttemptsExpression = "${retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${retry.max-delay}"))
    public OfferData getFinalizedOffer(UUID id) throws NoOfferException, WaitingException {
        var applicationDto = restTemplate.getForEntity("%s/%s".formatted(applicationsUrl, id), Application.class);
        var body = applicationDto.getBody();

        var applicationStatus = body.getStatus();
        if (applicationStatus == ApplicationStatus.DRAFT) {
            throw new WaitingException();
        }

        var offer = body.getOffer();
        if (applicationStatus == ApplicationStatus.PROCESSED && offer == null) {
            throw new NoOfferException();
        }
        return offer;
    }

    @Recover
    private OfferData recover(WaitingException ex) throws WaitingException {
        log.info("Was not able to retrieve processed request in given time.");
        throw new WaitingException("No response from provider");
    }

}

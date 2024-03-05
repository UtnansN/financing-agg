package com.example.financingagg.controller;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.integration.dto.OfferData;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping(value = "/getOffers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CompanyOffer> getOfferings(@RequestBody OfferRequest offerRequest) {
        return offerService.getOffers(offerRequest);
    }
}

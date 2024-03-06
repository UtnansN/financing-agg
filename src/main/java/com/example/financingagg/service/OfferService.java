package com.example.financingagg.service;

import com.example.financingagg.dto.CompanyOffer;
import com.example.financingagg.integration.dto.OfferData;
import com.example.financingagg.dto.OfferRequest;
import com.example.financingagg.integration.provider.OfferProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferProviderFactory offerProviderFactory;

    public Flux<CompanyOffer> getOffers(OfferRequest offerRequest) {
        var providers = offerProviderFactory.getProviders();

        return Flux.fromIterable(providers)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(provider -> Mono.defer(() -> Mono.just(provider.getOffer(offerRequest))))
                .sequential();
    }
}

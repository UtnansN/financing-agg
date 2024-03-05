package com.example.financingagg.integration.provider;

import com.example.financingagg.integration.provider.fastbank.FastBankOfferProvider;
import com.example.financingagg.integration.provider.solidbank.SolidBankOfferProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OfferProviderFactory {

    private final FastBankOfferProvider fastBankOfferProvider;
    private final SolidBankOfferProvider solidBankOfferProvider;

    public List<OfferProvider> getProviders() {
        return List.of(fastBankOfferProvider, solidBankOfferProvider);
    }
}

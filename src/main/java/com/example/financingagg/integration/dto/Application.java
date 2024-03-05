package com.example.financingagg.integration.dto;

import lombok.Data;

import java.util.UUID;

/**
 * In reality different companies may require different DTOs to get the same information.
 * In this case these would be defined in their respective packages and mappers would be used to translate to our Offer
 * Leaving it as a common thing here due to convenience.
 */
@Data
public class Application {
    private UUID id;

    private ApplicationStatus status;

    private OfferData offer;
}

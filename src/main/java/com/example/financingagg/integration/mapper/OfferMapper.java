package com.example.financingagg.integration.mapper;

import com.example.financingagg.dto.OfferRequest;

public interface OfferMapper<T> {
    T toApplicationRequestDto(OfferRequest requestDto);
}

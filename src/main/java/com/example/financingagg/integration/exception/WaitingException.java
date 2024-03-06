package com.example.financingagg.integration.exception;

import lombok.Getter;

@Getter
public class WaitingException extends RuntimeException {

    private String companyId;

    public WaitingException(String companyId) {
        this.companyId = companyId;
    }
}

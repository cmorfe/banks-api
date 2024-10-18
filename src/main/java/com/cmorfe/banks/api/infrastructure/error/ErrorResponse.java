package com.cmorfe.banks.api.infrastructure.error;

import lombok.Getter;

@Getter
public class ErrorResponse extends BaseErrorResponse {
    private final String details;

    public ErrorResponse(String message, String details) {
        super(message);
        this.details = details;
    }
}

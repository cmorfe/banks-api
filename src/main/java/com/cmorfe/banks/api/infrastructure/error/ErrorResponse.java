package com.cmorfe.banks.api.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    private String details;
}

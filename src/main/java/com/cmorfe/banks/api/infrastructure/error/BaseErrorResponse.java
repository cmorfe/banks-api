package com.cmorfe.banks.api.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseErrorResponse {
    private String message;
}
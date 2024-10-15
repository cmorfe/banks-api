package com.cmorfe.banks.api.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;

    private List<FieldError> errors;

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;

        private String error;
    }
}

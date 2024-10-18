package com.cmorfe.banks.api.infrastructure.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationErrorResponse extends BaseErrorResponse {
    private final List<FieldError> errors;

    public ValidationErrorResponse(String validationError, List<FieldError> errors) {
        super(validationError);
        this.errors = errors;
    }

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;

        private String error;
    }
}

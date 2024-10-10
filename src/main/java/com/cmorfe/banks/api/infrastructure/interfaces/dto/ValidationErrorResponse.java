package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;

    private List<FieldError> errors;

    @Data
    @AllArgsConstructor
    public static class FieldError {
        private String field;

        private String error;
    }
}

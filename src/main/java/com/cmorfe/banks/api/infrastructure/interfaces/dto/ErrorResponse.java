package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    private String details;
}

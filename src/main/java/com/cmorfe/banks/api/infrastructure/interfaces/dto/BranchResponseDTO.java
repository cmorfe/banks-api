package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchResponseDTO {
    private Long id;
    private String code;
    private String address;
    private String phone;
}

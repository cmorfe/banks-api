package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponseDTO {
    private Long id;
    private String code;
    private String address;
    private String phone;
}

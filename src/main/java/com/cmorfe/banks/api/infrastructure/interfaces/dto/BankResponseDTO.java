package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import com.cmorfe.banks.api.domain.model.BankType;
import lombok.*;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BankResponseDTO {
    private Long id;
    private String name;
    private BankType type;
    private List<BranchResponseDTO> branches;
}

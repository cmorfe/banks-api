package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import com.cmorfe.banks.api.domain.model.BankType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankResponseDTO {
    private Long id;
    private String name;
    private BankType type;
    private List<BranchResponseDTO> branches;
}

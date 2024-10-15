package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import com.cmorfe.banks.api.domain.model.BankType;
import com.cmorfe.banks.api.infrastructure.serialization.BankTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Schema(description = "Details about the bank")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BankRequestDTO {
    @Schema(description = "The name of the bank", example = "Bank of Example")
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Schema(description = "The type of the bank", example = "PRIVATE")
    @NotNull(message = "Type is required")
    @JsonDeserialize(using = BankTypeDeserializer.class)
    private BankType type;

    @Valid
    @Schema(description = "The list of branches of the bank")
    @NotEmpty(message = "The branches are required")
    private List<BranchRequestDTO> branches;
}

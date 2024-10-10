package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Details about the branch")
@Data
@AllArgsConstructor
public class BranchRequestDTO {
    @Schema(description = "The code of the branch", example = "3420")
    @NotBlank(message = "Code is required")
    @Size(max = 4, message = "Code cannot exceed 4 characters")
    private String code;

    @Schema(description = "The address of the branch", example = "456 Branch Avenue")
    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Schema(description = "The phone number of the branch", example = "11-7654-3210")
    @NotBlank(message = "Phone is required")
    @Size(max = 15, message = "Phone cannot exceed 15 characters")
    private String phone;
}

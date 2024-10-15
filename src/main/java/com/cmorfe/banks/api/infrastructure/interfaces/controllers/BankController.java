package com.cmorfe.banks.api.infrastructure.interfaces.controllers;

import com.cmorfe.banks.api.application.services.BankService;
import com.cmorfe.banks.api.infrastructure.error.ErrorResponse;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Banks API", description = "API for managing banks and their branches")
@RestController
@RequestMapping("/api/banks")
@Validated
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @Operation(summary = "Get all banks", responses = {
            @ApiResponse(responseCode = "200", description = "Banks found",
                    content = @Content(schema = @Schema(implementation = BankResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BankResponseDTO>> getAll() {
        List<BankResponseDTO> banks = bankService.getAll();

        return ResponseEntity.ok(banks);
    }

    @Operation(summary = "Get bank by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Bank found",
                    content = @Content(schema = @Schema(implementation = BankResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Bank not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BankResponseDTO> getById(@PathVariable Long id) {
        BankResponseDTO bank = bankService.getById(id);

        return ResponseEntity.ok(bank);
    }

    @Operation(summary = "Create a new bank", responses = {
            @ApiResponse(responseCode = "201", description = "Bank created",
                    content = @Content(schema = @Schema(implementation = BankResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate entry detected",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BankResponseDTO> create(@Valid @RequestBody BankRequestDTO bankRequestDTO) {
        BankResponseDTO bank = bankService.create(bankRequestDTO);

        URI location = URI.create("/banks/" + bank.getId());

        return ResponseEntity.created(location).body(bank);
    }

    @Operation(summary = "Update an existing bank", responses = {
            @ApiResponse(responseCode = "200", description = "Bank updated",
                    content = @Content(schema = @Schema(implementation = BankResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Bank not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BankResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody BankRequestDTO bankRequestDTO) {
        BankResponseDTO bank = bankService.update(id, bankRequestDTO);

        return ResponseEntity.ok(bank);
    }

    @Operation(summary = "Delete a bank", responses = {
            @ApiResponse(responseCode = "204", description = "Bank deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Bank not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bankService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consume the endpoint to Get all banks", responses = {
            @ApiResponse(responseCode = "200", description = "Banks found",
                    content = @Content(schema = @Schema(implementation = BankResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/consume")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BankResponseDTO>> consumeGetAll() {
        List<BankResponseDTO> banks = bankService.consumeGetAll();

        return ResponseEntity.ok(banks);
    }
}

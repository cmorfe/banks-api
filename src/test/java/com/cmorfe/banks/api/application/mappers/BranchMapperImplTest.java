package com.cmorfe.banks.api.application.mappers;

import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class BranchMapperImplTest {

    private final BranchMapperImpl branchMapper = new BranchMapperImpl();

    @Test
    void shouldReturnNullWhenBranchRequestDTOIsNull() {
        Branch result = branchMapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenBranchIsNull() {
        BranchResponseDTO result = branchMapper.toResponseDTO(null);
        assertNull(result);
    }
}
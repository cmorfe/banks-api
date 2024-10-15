package com.cmorfe.banks.api.application.mappers;

import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

class BankMapperImplTest {

    private final BankMapperImpl bankMapper = new BankMapperImpl();

    @Test
    void shouldReturnNullWhenBankRequestDTOIsNull() {
        Bank result = bankMapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenBankIsNull() {
        BankResponseDTO result = bankMapper.toResponseDTO(null);
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenBranchRequestDTOListIsNull() {
        List<Branch> result = bankMapper.branchRequestDTOListToBranchList(null);
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenBranchListIsNull() {
        List<BranchResponseDTO> result = bankMapper.branchListToBranchResponseDTOList(null);
        assertNull(result);
    }
}
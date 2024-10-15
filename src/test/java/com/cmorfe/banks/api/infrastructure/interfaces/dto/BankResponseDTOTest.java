package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import com.cmorfe.banks.api.domain.model.BankType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cmorfe.banks.api.util.TestUtils.*;

public class BankResponseDTOTest {

    @Test
    void shouldTestNoArgsConstructor() throws Exception {
        testNoArgsConstructor(BankResponseDTO.class);
    }

    @Test
    void shouldTestAllArgsConstructor() {
        BankResponseDTO dto = new BankResponseDTO(1L, "Bank of Example", BankType.PRIVATE, List.of(new BranchResponseDTO()));
        testAllArgsConstructor(dto, 1L, "Bank of Example", BankType.PRIVATE, List.of(new BranchResponseDTO()));
    }

    @Test
    void shouldTestSettersAndGetters() {
        BankResponseDTO dto = new BankResponseDTO();
        dto.setId(1L);
        dto.setName("Bank of Example");
        dto.setType(BankType.PRIVATE);
        dto.setBranches(List.of(new BranchResponseDTO()));

        testSettersAndGetters(dto, 1L, "Bank of Example", BankType.PRIVATE, List.of(new BranchResponseDTO()));
    }

    @Test
    void testEqualsAndHashCode() {
        class BankResponseTestDTO extends BankResponseDTO {
            public BankResponseTestDTO(Long id, String name, BankType type, List<BranchResponseDTO> branches) {
                super(id, name, type, branches);
            }

            @Override
            public boolean canEqual(Object obj) {
                return false;
            }
        }

        verifyEqualsAndHashCode(BankResponseDTO.class, BankResponseTestDTO.class);
    }
}
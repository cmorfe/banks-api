package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import com.cmorfe.banks.api.domain.model.BankType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.cmorfe.banks.api.util.TestUtils.verifyEqualsAndHashCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BankResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        BankResponseDTO dto = new BankResponseDTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        BankResponseDTO dto = new BankResponseDTO(1L, "Bank of Example", BankType.PRIVATE, List.of(new BranchResponseDTO()));
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Bank of Example", dto.getName());
        assertEquals(BankType.PRIVATE, dto.getType());
        assertNotNull(dto.getBranches());
    }

    @Test
    void testSettersAndGetters() {
        BankResponseDTO dto = new BankResponseDTO();
        dto.setId(1L);
        dto.setName("Bank of Example");
        dto.setType(BankType.PRIVATE);
        dto.setBranches(List.of(new BranchResponseDTO()));

        assertEquals(1L, dto.getId());
        assertEquals("Bank of Example", dto.getName());
        assertEquals(BankType.PRIVATE, dto.getType());
        assertNotNull(dto.getBranches());
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
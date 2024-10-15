package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import org.junit.jupiter.api.Test;

import static com.cmorfe.banks.api.util.TestUtils.*;

public class BranchResponseDTOTest {

    @Test
    void shouldTestNoArgsConstructor() throws Exception {
        testNoArgsConstructor(BranchResponseDTO.class);
    }

    @Test
    void shouldTestAllArgsConstructor() {
        BranchResponseDTO dto = new BranchResponseDTO(1L, "3420", "456 Branch Avenue", "11-7654-3210");
        testAllArgsConstructor(dto, 1L, "3420", "456 Branch Avenue", "11-7654-3210");
    }

    @Test
    void shouldTestSettersAndGetters() {
        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(1L);
        dto.setCode("3420");
        dto.setAddress("456 Branch Avenue");
        dto.setPhone("11-7654-3210");

        testSettersAndGetters(dto, 1L, "3420", "456 Branch Avenue", "11-7654-3210");
    }

    @Test
    void testEqualsAndHashCode() {
        class BranchResponseTestDTO extends BranchResponseDTO {
            public BranchResponseTestDTO(Long id, String code, String address, String phone) {
                super(id, code, address, phone);
            }

            @Override
            public boolean canEqual(Object obj) {
                return false;
            }
        }

        verifyEqualsAndHashCode(BranchResponseDTO.class, BranchResponseTestDTO.class);
    }
}
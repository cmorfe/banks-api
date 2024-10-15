package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import org.junit.jupiter.api.Test;

import static com.cmorfe.banks.api.util.TestUtils.verifyEqualsAndHashCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BranchResponseDTOTest {

    @Test
    void testNoArgsConstructor() {
        BranchResponseDTO dto = new BranchResponseDTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        BranchResponseDTO dto = new BranchResponseDTO(1L, "3420", "456 Branch Avenue", "11-7654-3210");
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("3420", dto.getCode());
        assertEquals("456 Branch Avenue", dto.getAddress());
        assertEquals("11-7654-3210", dto.getPhone());
    }

    @Test
    void testSettersAndGetters() {
        BranchResponseDTO dto = new BranchResponseDTO();
        dto.setId(1L);
        dto.setCode("3420");
        dto.setAddress("456 Branch Avenue");
        dto.setPhone("11-7654-3210");

        assertEquals(1L, dto.getId());
        assertEquals("3420", dto.getCode());
        assertEquals("456 Branch Avenue", dto.getAddress());
        assertEquals("11-7654-3210", dto.getPhone());
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
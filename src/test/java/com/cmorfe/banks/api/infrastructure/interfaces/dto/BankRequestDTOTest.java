package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import com.cmorfe.banks.api.domain.model.BankType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankRequestDTOTest {

    @Test
    public void equalsAndHashCodeContract() {
        class BankRequestTestDTO extends BankRequestDTO {
            public BankRequestTestDTO(String name, BankType type, List<BranchRequestDTO> branches) {
                super(name, type, branches);
            }

            @Override
            public boolean canEqual(Object obj) {
                return false;
            }
        }

        EqualsVerifier.forClass(BankRequestDTO.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(BankRequestTestDTO.class)
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    void testNoArgsConstructor() {
        BankRequestDTO dto = new BankRequestDTO();
        assertNotNull(dto);
    }

    @Test
    void testCanEqual() {
        BankRequestDTO dto1 = new BankRequestDTO();
        BankRequestDTO dto2 = new BankRequestDTO();
        Object otherObject = new Object();

        assertTrue(dto1.canEqual(dto2));
        assertFalse(dto1.canEqual(otherObject));
    }

    @Test
    void testEqualsWithDifferentClass() {
        BankRequestDTO dto1 = new BankRequestDTO("Bank1", BankType.PUBLIC, List.of(new BranchRequestDTO()));
        Object otherObject = new Object();

        assertNotEquals(dto1, otherObject);
    }

    @Test
    void testHashCodeWithNullType() {
        BankRequestDTO dto = new BankRequestDTO("Bank1", null, List.of(new BranchRequestDTO()));
        int hashCode = dto.hashCode();
        assertTrue(hashCode != 0);
    }

    @Test
    void testEqualsWithNullType() {
        BankRequestDTO dto1 = new BankRequestDTO("Bank1", null, List.of(new BranchRequestDTO()));
        BankRequestDTO dto2 = new BankRequestDTO("Bank1", BankType.PUBLIC, List.of(new BranchRequestDTO()));
        BankRequestDTO dto3 = new BankRequestDTO("Bank1", null, List.of(new BranchRequestDTO()));

        assertNotEquals(dto1, dto2);
        assertEquals(dto1, dto3);
    }
}
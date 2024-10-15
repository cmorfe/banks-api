package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BranchRequestDTOTest {

    private final Validator validator;

    public BranchRequestDTOTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testNoArgsConstructor() {
        BranchRequestDTO dto = new BranchRequestDTO();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        BranchRequestDTO dto = new BranchRequestDTO("3420", "456 Branch Avenue", "11-7654-3210");
        assertNotNull(dto);
        assertEquals("3420", dto.getCode());
        assertEquals("456 Branch Avenue", dto.getAddress());
        assertEquals("11-7654-3210", dto.getPhone());
    }

    @Test
    void testValidation() {
        BranchRequestDTO dto = new BranchRequestDTO("", "", "");
        Set<ConstraintViolation<BranchRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }

    @Test
    public void equalsAndHashCodeContract() {
        class BranchRequestTestDTO extends BranchRequestDTO {
            public BranchRequestTestDTO(String code, String address, String phone) {
                super(code, address, phone);
            }

            @Override
            public boolean canEqual(Object obj) {
                return false;
            }
        }

        EqualsVerifier.forClass(BranchRequestDTO.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(BranchRequestTestDTO.class)
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    void testCodeValidation() {
        BranchRequestDTO dto = new BranchRequestDTO("12345", "456 Branch Avenue", "11-7654-3210");
        Set<ConstraintViolation<BranchRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("code")));
    }

    @Test
    void testAddressValidation() {
        BranchRequestDTO dto = new BranchRequestDTO("3420", "", "11-7654-3210");
        Set<ConstraintViolation<BranchRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("address")));
    }

    @Test
    void testPhoneValidation() {
        BranchRequestDTO dto = new BranchRequestDTO("3420", "456 Branch Avenue", "");
        Set<ConstraintViolation<BranchRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
    }
}
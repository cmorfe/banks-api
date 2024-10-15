package com.cmorfe.banks.api.infrastructure.interfaces.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import java.util.Set;

import static com.cmorfe.banks.api.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class BranchRequestDTOTest {

    private final Validator validator;

    public BranchRequestDTOTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldTestNoArgsConstructor() throws Exception {
        testNoArgsConstructor(BranchRequestDTO.class);
    }

    @Test
    void shouldTestAllArgsConstructor() {
        BranchRequestDTO dto = new BranchRequestDTO("3420", "456 Branch Avenue", "11-7654-3210");
        testAllArgsConstructor(dto, "3420", "456 Branch Avenue", "11-7654-3210");
    }

    @Test
    void testValidation() {
        BranchRequestDTO dto = new BranchRequestDTO("", "", "");
        Set<ConstraintViolation<BranchRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }

    @Test
    void testEqualsAndHashCode() {
        class BranchRequestTestDTO extends BranchRequestDTO {
            public BranchRequestTestDTO(String code, String address, String phone) {
                super(code, address, phone);
            }

            @Override
            public boolean canEqual(Object obj) {
                return false;
            }
        }

        verifyEqualsAndHashCode(BranchRequestDTO.class, BranchRequestTestDTO.class);
    }
}
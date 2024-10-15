package com.cmorfe.banks.api.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static com.cmorfe.banks.api.util.TestUtils.createBank;
import static com.cmorfe.banks.api.util.TestUtils.createBranch;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private static final String BANK_1_NAME = "Bank1";
    private static final String BANK_2_NAME = "Bank2";

    private Bank bank1;

    private Bank bank2;

    private Bank bank3;

    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();

        bank1 = createBank(1L, BANK_1_NAME, BankType.PUBLIC, now);

        bank2 = createBank(1L, BANK_1_NAME, BankType.PUBLIC, now);

        bank3 = createBank(2L, BANK_2_NAME, BankType.PUBLIC, now);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(bank1, bank3);
    }

    @Test
    void testEqualsWithSelf() {
        assertEquals(bank1, bank1);
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(bank1, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(bank1, new Object());
    }

    @Test
    void testEqualsWithDifferentId() {
        bank2.setId(2L);
        assertNotEquals(bank1, bank2);
    }

    @Test
    void testEqualsWithDifferentName() {
        bank2.setName(BANK_2_NAME);
        assertNotEquals(bank1, bank2);
    }

    @Test
    void testEqualsWithDifferentType() {
        bank2.setType(BankType.PRIVATE);
        assertNotEquals(bank1, bank2);
    }

    @Test
    void testEqualsWithDifferentCreatedAt() {
        bank2.setCreatedAt(now.minusSeconds(1000));
        assertNotEquals(bank1, bank2);
    }

    @Test
    void testEqualsWithDifferentUpdatedAt() {
        bank2.setUpdatedAt(now.minusSeconds(1000));
        assertNotEquals(bank1, bank2);
    }

    @Test
    void testEqualsWithDifferentBranches() {
        bank2.setBranches(List.of(createBranch(2L, now)));

        assertNotEquals(bank1, bank2);
    }

    @Test
    void testSetId() {
        bank1.setId(2L);
        assertEquals(2L, bank1.getId());
    }

    @Test
    void testSetCreatedAt() {
        bank1.setCreatedAt(now);
        assertEquals(now, bank1.getCreatedAt());
    }

    @Test
    void testSetUpdatedAt() {
        bank1.setUpdatedAt(now);
        assertEquals(now, bank1.getUpdatedAt());
    }
}
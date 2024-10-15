package com.cmorfe.banks.api.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.cmorfe.banks.api.util.TestUtils.createBank;
import static com.cmorfe.banks.api.util.TestUtils.createBranch;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchTest {
    private Branch branch;
    private Bank bank;
    private Instant now;
    private Long id;

    @BeforeEach
    void setUp() {
        id = 1L;
        now = Instant.now();
        branch = createBranch(id, now);
        branch.setCreatedAt(now);
        branch.setUpdatedAt(now);
        bank = createBank(id, "Bank 1", BankType.PUBLIC, now);
        branch.setBank(bank);
    }

    @Test
    void testSetId() {
        branch.setId(id);
        assertEquals(id, branch.getId());
    }

    @Test
    void testGetCreatedAt() {
        assertEquals(now, branch.getCreatedAt());
    }

    @Test
    void testGetUpdatedAt() {
        assertEquals(now, branch.getUpdatedAt());
    }

    @Test
    void testGetBank() {
        assertEquals(bank, branch.getBank());
    }
}
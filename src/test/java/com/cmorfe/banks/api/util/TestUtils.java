package com.cmorfe.banks.api.util;

import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.domain.model.BankType;
import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchResponseDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtils {

    public static Branch createBranch(Long id, Instant now) {
        return new Branch(id, String.valueOf(id), "Address 1", "Phone 1", now, now, null);
    }

    public static Bank createBank(Long id, String name, BankType type, Instant now) {
        Branch branch = createBranch(id, now);

        Bank bank = new Bank(id, name, type, now, now, new ArrayList<>(List.of(branch)));

        branch.setBank(bank);

        return bank;
    }

    public static BankRequestDTO createBankRequestDTO(Bank bank) {
        return new BankRequestDTO(bank.getName(), bank.getType(), bank.getBranches().stream()
                .map(branch -> new BranchRequestDTO(branch.getCode(), branch.getAddress(), branch.getPhone()))
                .collect(Collectors.toList()));
    }

    public static BankResponseDTO createBankResponseDTO(Bank bank) {
        return new BankResponseDTO(bank.getId(), bank.getName(), bank.getType(), bank.getBranches().stream()
                .map(branch -> new BranchResponseDTO(branch.getId(), branch.getCode(), branch.getAddress(), branch.getPhone()))
                .collect(Collectors.toList()));
    }

    public static Bank getUpdateBankWithNewBranch(long bankId, String name, long branchId, Instant now) {
        Bank bank = createBank(bankId, name, BankType.PUBLIC, now);

        Branch newBranch = createBranch(branchId, now);

        newBranch.setBank(bank);

        bank.getBranches().add(newBranch);

        return bank;
    }

    public static <T> void verifyEqualsAndHashCode(Class<T> clazz, Class<? extends T> subclass) {
        EqualsVerifier.forClass(clazz)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(subclass)
                .withRedefinedSuperclass()
                .verify();
    }

    public static <T> void testNoArgsConstructor(Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        assertNotNull(instance);
    }

    public static <T> void testAllArgsConstructor(T instance, Object... expectedValues) {
        assertNotNull(instance);
        for (Object expectedValue : expectedValues) {
            assertNotNull(expectedValue);
        }
    }

    public static <T> void testSettersAndGetters(T instance, Object... expectedValues) {
        for (Object expectedValue : expectedValues) {
            assertNotNull(expectedValue);
        }
    }
}
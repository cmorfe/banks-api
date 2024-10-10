package com.cmorfe.banks.api.util;

import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.domain.model.BankType;
import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchResponseDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestDataUtils {

    public static Branch createBranch(Long id) {
        return new Branch(id, String.valueOf(id), "Address 1", "Phone 1", Instant.now(), Instant.now(), null);
    }

    public static Bank createBank(Long id, String name, BankType type) {
        Branch branch = createBranch(id);

        Bank bank = new Bank(id, name, type, Instant.now(), Instant.now(), new ArrayList<>(List.of(branch)));

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

    public static Bank getUpdateBankWithNewBranch(long bankId, String name, long branchId) {
        Bank bank = createBank(bankId, name, BankType.PUBLIC);

        Branch newBranch = createBranch(branchId);

        newBranch.setBank(bank);

        bank.getBranches().add(newBranch);

        return bank;
    }
}
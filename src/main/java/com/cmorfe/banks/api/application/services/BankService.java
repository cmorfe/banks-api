package com.cmorfe.banks.api.application.services;

import com.cmorfe.banks.api.application.mappers.BankMapper;
import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.domain.repository.BankRepository;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankService {
    private static final String BANK_ID_NOT_FOUND = "Bank not found with id: ";

    private final BankRepository bankRepository;

    private final RestTemplate restTemplate;

    @Value("${banks.api.url}")
    private String banksApiUrl;

    public BankService(BankRepository bankRepository, RestTemplate restTemplate) {
        this.bankRepository = bankRepository;

        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<BankResponseDTO> getAll() {
        List<Bank> banks = bankRepository.findAll();

        return banks.stream()
                .map(BankMapper.INSTANCE::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public BankResponseDTO getById(Long id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BANK_ID_NOT_FOUND + id));

        return BankMapper.INSTANCE.toResponseDTO(bank);
    }

    @Transactional
    public BankResponseDTO create(BankRequestDTO bankRequestDTO) {
        Bank bank = BankMapper.INSTANCE.toEntity(bankRequestDTO);

        bank = createBank(bank);

        return BankMapper.INSTANCE.toResponseDTO(bank);
    }

    private Bank createBank(Bank bank) {
        bank.getBranches().forEach(branch -> branch.setBank(bank));

        return bankRepository.save(bank);
    }

    @Transactional
    public BankResponseDTO update(Long id, BankRequestDTO bankRequestDTO) {
        Bank bank = BankMapper.INSTANCE.toEntity(bankRequestDTO);

        bank = findAndUpdateBank(id, bank);

        return BankMapper.INSTANCE.toResponseDTO(bank);
    }

    private Bank findAndUpdateBank(Long id, Bank updateData) {
        return bankRepository.findById(id)
                .map(bank -> updateBank(bank, updateData))
                .orElseThrow(() -> new EntityNotFoundException(BANK_ID_NOT_FOUND + id));
    }

    private Bank updateBank(Bank bank, Bank updateData) {
        updateBankBranches(bank, updateData);

        updateBankData(bank, updateData);

        return bankRepository.save(bank);
    }

    private void updateBankBranches(Bank bank, Bank updateData) {
        Map<String, Branch> currentBranches = getCurrentBranches(bank);

        updateOrAddBranches(bank, updateData, currentBranches);

        removeOrphanBranches(bank, currentBranches);
    }

    private Map<String, Branch> getCurrentBranches(Bank bank) {
        return bank.getBranches().stream()
                .collect(Collectors.toMap(Branch::getCode, branch -> branch));
    }

    private void updateOrAddBranches(Bank bank, Bank updateData, Map<String, Branch> currentBranches) {
        updateData.getBranches().forEach(updateBranchData -> {
            Branch existingBranch = currentBranches.get(updateBranchData.getCode());

            if (existingBranch != null) {
                updateBranch(existingBranch, updateBranchData);
            } else {
                addNewBranch(bank, updateBranchData);
            }

            currentBranches.remove(updateBranchData.getCode());
        });
    }

    private void removeOrphanBranches(Bank bank, Map<String, Branch> currentBranchesMap) {
        currentBranchesMap.values().forEach(bank.getBranches()::remove);
    }

    private void updateBankData(Bank bank, Bank updateData) {
        bank.setName(updateData.getName());
        bank.setType(updateData.getType());
    }

    private void updateBranch(Branch branch, Branch updateBranchData) {
        branch.setAddress(updateBranchData.getAddress());
        branch.setPhone(updateBranchData.getPhone());
    }

    private void addNewBranch(Bank bank, Branch newBranch) {
        newBranch.setBank(bank);
        bank.getBranches().add(newBranch);
    }

    @Transactional
    public void delete(Long id) {
        if (!bankRepository.existsById(id)) {
            throw new EntityNotFoundException(BANK_ID_NOT_FOUND + id);
        }

        bankRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BankResponseDTO> consumeGetAll() {
        ResponseEntity<List<BankResponseDTO>> response = restTemplate.exchange(
                banksApiUrl + "/api/banks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }
}

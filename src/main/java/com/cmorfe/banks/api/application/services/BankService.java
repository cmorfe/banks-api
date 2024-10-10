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
        Bank entity = BankMapper.INSTANCE.toEntity(bankRequestDTO);

        entity.getBranches().forEach(branch -> branch.setBank(entity));

        Bank bank = bankRepository.save(entity);

        return BankMapper.INSTANCE.toResponseDTO(bank);
    }

    @Transactional
    public BankResponseDTO update(Long id, BankRequestDTO bankRequestDTO) {
        Bank entity = BankMapper.INSTANCE.toEntity(bankRequestDTO);

        Bank updatedBank = bankRepository.findById(id)
                .map(bank -> updateBank(bank, entity))
                .orElseThrow(() -> new EntityNotFoundException(BANK_ID_NOT_FOUND + id));

        return BankMapper.INSTANCE.toResponseDTO(updatedBank);
    }

    private Bank updateBank(Bank bank, Bank updateData) {
        removeOrphanBranches(bank, updateData);

        updateBankData(bank, updateData);

        return bankRepository.save(bank);
    }

    private void removeOrphanBranches(Bank bank, Bank updateData) {
        List<Branch> orphanBranches = bank.getBranches().stream()
                .filter(branch -> updateData.getBranches().stream()
                        .noneMatch(updateBranch -> updateBranch.getCode().equals(branch.getCode())))
                .toList();

        orphanBranches.forEach(branch -> bank.getBranches().remove(branch));
    }

    private void updateBankData(Bank bank, Bank updateData) {
        bank.setName(updateData.getName());
        bank.setType(updateData.getType());

        for (Branch updateBranchData : updateData.getBranches()) {
            bank.getBranches().stream()
                    .filter(branch -> branch.getCode().equals(updateBranchData.getCode()))
                    .findFirst()
                    .ifPresentOrElse(branch -> {
                        branch.setAddress(updateBranchData.getAddress());
                        branch.setPhone(updateBranchData.getPhone());
                    }, () -> {
                        updateBranchData.setBank(bank);
                        bank.getBranches().add(updateBranchData);
                    });
        }
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

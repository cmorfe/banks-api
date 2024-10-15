package com.cmorfe.banks.api.application.services;

import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.domain.model.BankType;
import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.domain.repository.BankRepository;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.cmorfe.banks.api.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
class BankServiceTest {
    private static final String API_BANKS_PATH = "/api/banks";
    private static final String BANK_ID_NOT_FOUND = "Bank not found with id: ";
    private static final String UPDATED_BANK_NAME = "Updated Bank";
    private static final String BANK_1_NAME = "Bank 1";
    private static final String BANK_2_NAME = "Bank 2";

    @Mock
    private BankRepository bankRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BankService bankService;

    @Value("${banks.api.url}")
    private String banksApiUrl;

    private Long id;

    private Bank bank;

    private List<Bank> banks;

    private BankResponseDTO bankResponseDTO;

    private List<BankResponseDTO> banksResponseDTO;

    private Bank updatedBank;

    private BankRequestDTO updatedBankRequestDTO;

    private BankResponseDTO updatedBankResponseDTO;

    private Instant now;

    @BeforeEach
    void setUp() {
        id = 1L;

        now = Instant.now();

        bank = createBank(id, BANK_1_NAME, BankType.PUBLIC, now);

        Bank bank2 = createBank(2L, BANK_2_NAME, BankType.PRIVATE, now);

        banks = Arrays.asList(bank, bank2);

        bankResponseDTO = createBankResponseDTO(bank);

        banksResponseDTO = Arrays.asList(
                bankResponseDTO,
                createBankResponseDTO(bank2)
        );

        updatedBank = createBank(id, UPDATED_BANK_NAME, BankType.PUBLIC, now);

        updatedBankRequestDTO = createBankRequestDTO(updatedBank);

        updatedBankResponseDTO = createBankResponseDTO(updatedBank);
    }

    @Nested
    class GetTests {
        @Test
        void testGetAll() {
            when(bankRepository.findAll()).thenReturn(banks);

            List<BankResponseDTO> actualResponse = bankService.getAll();

            assertEquals(banksResponseDTO, actualResponse);
        }

        @Test
        void testGetById() {
            when(bankRepository.findById(id)).thenReturn(Optional.of(bank));

            BankResponseDTO actualResponse = bankService.getById(id);

            assertEquals(bankResponseDTO, actualResponse);
        }

        @Test
        void testGetByIdNotFound() {
            when(bankRepository.findById(id)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bankService.getById(id));

            assertEquals(BANK_ID_NOT_FOUND + id, exception.getMessage());
        }
    }

    @Nested
    class CreateTests {
        @Test
        void testCreate() {
            Bank newBank = createBank(null, BANK_1_NAME, BankType.PUBLIC, now);

            doReturn(bank).when(bankRepository).save(any(Bank.class));

            BankRequestDTO newBankRequestDTO = createBankRequestDTO(newBank);

            BankResponseDTO actualResponse = bankService.create(newBankRequestDTO);

            assertEquals(bankResponseDTO, actualResponse);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void testUpdate() {
            when(bankRepository.findById(id)).thenReturn(Optional.of(bank));

            when(bankRepository.save(any(Bank.class))).thenReturn(updatedBank);

            BankResponseDTO actualResponse = bankService.update(id, updatedBankRequestDTO);

            assertEquals(updatedBankResponseDTO, actualResponse);
        }

        @Test
        void testUpdateNotFound() {
            when(bankRepository.findById(id)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> bankService.update(id, updatedBankRequestDTO));

            assertEquals(BANK_ID_NOT_FOUND + id, exception.getMessage());
        }

        @Test
        void testUpdateBankWithNewBranch() {
            long branchId = 3L;

            Bank updateBank = getUpdateBankWithNewBranch(id, BANK_1_NAME, branchId, now);

            when(bankRepository.findById(id)).thenReturn(Optional.of(bank));

            when(bankRepository.save(any(Bank.class))).thenReturn(updateBank);

            bankService.update(id, createBankRequestDTO(updateBank));
            assertTrue(updateBank.getBranches().stream().anyMatch(branch -> (branchId == branch.getId())));
        }

        @Test
        void testUpdateBankRemovesOrphanBranches() {
            Branch branchToRemove = createBranch(2L, now);
            branchToRemove.setBank(bank);
            bank.getBranches().add(branchToRemove);

            Bank updateData = createBank(id, BANK_1_NAME, BankType.PUBLIC, now);

            when(bankRepository.findById(id)).thenReturn(Optional.of(bank));

            when(bankRepository.save(any(Bank.class))).thenReturn(updateData);

            bankService.update(id, createBankRequestDTO(updateData));

            assertFalse(bank.getBranches().contains(branchToRemove));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void testDelete() {
            when(bankRepository.existsById(id)).thenReturn(true);

            assertDoesNotThrow(() -> bankService.delete(id));
        }

        @Test
        void testDeleteNotFound() {
            when(bankRepository.existsById(id)).thenReturn(false);

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bankService.delete(id));

            assertEquals(BANK_ID_NOT_FOUND + id, exception.getMessage());
        }
    }

    @Nested
    class ConsumeTests {
        @Test
        void testConsumeGetAll() {
            String apiUrl = banksApiUrl + API_BANKS_PATH;

            doReturn(ResponseEntity.ok(banksResponseDTO)).when(restTemplate).exchange(
                    eq(apiUrl),
                    eq(HttpMethod.GET),
                    isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<List<BankResponseDTO>>>any()
            );

            List<BankResponseDTO> actualResponse = bankService.consumeGetAll();

            assertEquals(banksResponseDTO, actualResponse);
        }
    }
}
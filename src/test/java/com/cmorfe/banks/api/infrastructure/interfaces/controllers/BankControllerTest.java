package com.cmorfe.banks.api.infrastructure.interfaces.controllers;

import com.cmorfe.banks.api.application.services.BankService;
import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.domain.model.BankType;
import com.cmorfe.banks.api.infrastructure.configuration.GlobalExceptionHandler;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import com.cmorfe.banks.api.util.JsonResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static com.cmorfe.banks.api.util.TestUtils.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = BankController.class)
@ContextConfiguration(classes = {GlobalExceptionHandler.class, BankController.class})
class BankControllerTest {

    private static final String UPDATED_BANK_NAME = "Updated Bank";
    private static final String BANK_ID_NOT_FOUND = "Bank not found with id: ";
    private static final String BANK_1_NAME = "Bank 1";
    private static final String BANK_2_NAME = "Bank 2";
    private static final String API_BANKS = "/api/banks";
    private static final String API_BANKS_ID = "/api/banks/{id}";

    @MockBean
    private BankService bankService;

    private MockMvc mockMvc;

    private Long id;

    private List<BankResponseDTO> banks;

    private Bank updatedBank;

    private BankRequestDTO bankRequestDTO;

    private BankResponseDTO bankResponseDTO;

    private BankRequestDTO updatedBankRequestDTO;

    @BeforeEach
    void setUp() {
        id = 1L;

        Instant now = Instant.now();

        updatedBank = createBank(id, UPDATED_BANK_NAME, BankType.PUBLIC, now);

        Bank bank1 = createBank(id, BANK_1_NAME, BankType.PUBLIC, now);

        bankRequestDTO = createBankRequestDTO(bank1);

        updatedBankRequestDTO = createBankRequestDTO(updatedBank);

        bankResponseDTO = createBankResponseDTO(bank1);

        Bank bank2 = createBank(2L, BANK_2_NAME, BankType.PRIVATE, now);

        BankResponseDTO bankResponseDTO2 = createBankResponseDTO(bank2);

        banks = List.of(bankResponseDTO, bankResponseDTO2);

        mockMvc = MockMvcBuilders.standaloneSetup(new BankController(bankService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    class GetTests {
        @Test
        void testGetAll() throws Exception {
            when(bankService.getAll()).thenReturn(banks);

            mockMvc.perform(get(API_BANKS))
                    .andExpect(status().isOk())
                    .andExpect(JsonResultMatchers.jsonEquals(banks));
        }

        @Test
        void testGetById() throws Exception {
            when(bankService.getById(id)).thenReturn(bankResponseDTO);

            mockMvc.perform(get(API_BANKS_ID, id))
                    .andExpect(status().isOk())
                    .andExpect(JsonResultMatchers.jsonEquals(bankResponseDTO));
        }

        @Test
        void testGetByIdNotFound() throws Exception {
            when(bankService.getById(id)).thenThrow(new EntityNotFoundException(BANK_ID_NOT_FOUND + id));

            mockMvc.perform(get(API_BANKS_ID, id))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateTests {
        @Test
        void testCreate() throws Exception {
            when(bankService.create(bankRequestDTO)).thenReturn(bankResponseDTO);

            mockMvc.perform(post(API_BANKS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(bankRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(JsonResultMatchers.jsonEquals(bankResponseDTO));
        }

        @Test
        void testCreateWithBadRequest() throws Exception {
            BankRequestDTO invalidRequestDTO = new BankRequestDTO();

            mockMvc.perform(post(API_BANKS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(invalidRequestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void testUpdate() throws Exception {
            BankResponseDTO updatedBankResponseDTO = createBankResponseDTO(updatedBank);

            when(bankService.update(id, updatedBankRequestDTO)).thenReturn(updatedBankResponseDTO);

            mockMvc.perform(put(API_BANKS_ID, id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(updatedBankRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(JsonResultMatchers.jsonEquals(updatedBankResponseDTO));
        }

        @Test
        void testUpdateNotFound() throws Exception {
            when(bankService.update(id, updatedBankRequestDTO))
                    .thenThrow(new EntityNotFoundException(BANK_ID_NOT_FOUND + id));

            mockMvc.perform(put(API_BANKS_ID, id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(updatedBankRequestDTO)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void testDelete() throws Exception {
            mockMvc.perform(delete(API_BANKS_ID, id))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testDeleteNotFound() throws Exception {
            doThrow(new EntityNotFoundException(BANK_ID_NOT_FOUND + id)).when(bankService).delete(id);

            mockMvc.perform(delete(API_BANKS_ID, id))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testConsumeGetAll() throws Exception {
            when(bankService.consumeGetAll()).thenReturn(banks);

            mockMvc.perform(get("/api/banks/consume"))
                    .andExpect(status().isOk())
                    .andExpect(JsonResultMatchers.jsonEquals(banks));
        }
    }
}

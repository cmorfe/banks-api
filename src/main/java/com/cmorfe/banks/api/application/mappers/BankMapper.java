package com.cmorfe.banks.api.application.mappers;

import com.cmorfe.banks.api.domain.model.Bank;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BankResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = BranchMapper.class)
public interface BankMapper {
    BankMapper INSTANCE = Mappers.getMapper(BankMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "branches", target = "branches")
    Bank toEntity(BankRequestDTO bankRequestDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "branches", target = "branches")
    BankResponseDTO toResponseDTO(Bank bank);
}

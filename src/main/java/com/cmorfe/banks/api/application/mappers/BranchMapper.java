package com.cmorfe.banks.api.application.mappers;

import com.cmorfe.banks.api.domain.model.Branch;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchRequestDTO;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.BranchResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BranchMapper {
    BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

    @Mapping(source = "code", target = "code")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    Branch toEntity(BranchRequestDTO branchRequestDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    BranchResponseDTO toResponseDTO(Branch branch);
}

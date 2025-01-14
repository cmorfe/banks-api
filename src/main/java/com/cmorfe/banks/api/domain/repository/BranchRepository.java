package com.cmorfe.banks.api.domain.repository;

import com.cmorfe.banks.api.domain.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}

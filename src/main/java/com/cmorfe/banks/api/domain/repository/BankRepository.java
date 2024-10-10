package com.cmorfe.banks.api.domain.repository;

import com.cmorfe.banks.api.domain.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}

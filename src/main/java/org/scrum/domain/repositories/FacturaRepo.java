package org.scrum.domain.repositories;

import org.scrum.domain.models.FacturaMdl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacturaRepo extends JpaRepository<FacturaMdl, Long> {

    Optional<FacturaMdl> findByInvoiceNr(String number);

    List<FacturaMdl> findByClient_NameContainingAndFurnizor_NameContainingAndInvoiceNrContaining(
            String clientName, String furnizorName, String fileName);
}


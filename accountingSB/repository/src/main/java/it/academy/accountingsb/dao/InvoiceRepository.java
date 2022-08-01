package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>,
        JpaSpecificationExecutor {
}

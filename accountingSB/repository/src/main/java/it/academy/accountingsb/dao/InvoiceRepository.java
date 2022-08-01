package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.Equipment;
import it.academy.accountingsb.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>,
        JpaSpecificationExecutor {

    @Query("SELECT i.equipments FROM Invoice i where i.id = :idInvoice")
    List<Equipment> showEquipmentsForInvoice(@Param("idInvoice") Integer idInvoice);
}

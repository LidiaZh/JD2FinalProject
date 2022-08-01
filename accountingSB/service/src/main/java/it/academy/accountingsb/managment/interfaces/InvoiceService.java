package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.InvoiceDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceService {

    InvoiceDto saveInvoice(InvoiceDto invoiceDto,
                           Integer idSupplier,
                           Integer idReceiver);

    void delInvoice(Integer idInvoice);

    InvoiceDto getInvoice(Integer idInvoice);

    List<InvoiceDto> getListOfInvoiceDto();

    Page<InvoiceDto> invoiceByPage(int pageSize,
                                   int pageNum,
                                   String sortField,
                                   String sortDir);

//    Invoice writeNewInvoice(Integer number, LocalDate date,
//                            String cause, Integer idSupplier,
//                            Integer idReceiver);

    void getEquipmentForInvoice(String[] equipments,
                                Integer idInvoice);
}

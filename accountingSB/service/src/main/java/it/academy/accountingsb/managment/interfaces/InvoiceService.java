package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.EquipmentDto;
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

    void getEquipmentForInvoice(String[] equipments,
                                Integer idInvoice);

    List<EquipmentDto> getEquipment(Integer idInvoice);
}

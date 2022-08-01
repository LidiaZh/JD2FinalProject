package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.InvoiceDto;
import it.academy.accountingsb.entity.Invoice;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.xml.validation.Validator;

@Mapper(componentModel = "spring",
        uses = {OrganizationMapper.class,
                Validator.class})
public interface InvoiceMapper {

    InvoiceDto toInvoiceDto(Invoice invoice);

    Invoice toInvoice(InvoiceDto invoiceDto);


}

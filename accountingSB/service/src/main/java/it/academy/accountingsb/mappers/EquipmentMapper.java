package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.EquipmentDto;
import it.academy.accountingsb.entity.Equipment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.validation.Validator;

@Mapper(componentModel = "spring",
        uses = {InvoiceMapper.class,
                EquipmentMapper.class,
                ResponsiblePersonMapper.class,
                Validator.class})
public interface EquipmentMapper {

    @Mapping(source = "invoices", target = "invoicesForEquipment")
    @Mapping(target = "idInvoiceLast", ignore = true)
    @Mapping(target = "invoiceNumberLast", ignore = true)
    EquipmentDto toEquipmentDto(Equipment equipment);

    @InheritInverseConfiguration
    Equipment toEquipment(EquipmentDto equipmentDto);

    @BeforeMapping
    default void findIdInvoiceLast(Equipment equipment, @MappingTarget EquipmentDto equipmentDto) {
        equipmentDto.setIdInvoiceLast(equipment.getInvoices().get((equipment.getInvoices()).size() - 1).getId());
    }

    @BeforeMapping
    default void findInvoiceNumberLast(Equipment equipment, @MappingTarget EquipmentDto equipmentDto) {
        equipmentDto.setInvoiceNumberLast(equipment.getInvoices().get((equipment.getInvoices()).size() - 1).getNumber());
    }

}

package it.academy.accountingsb.managment.interfaces;


import it.academy.accountingsb.dto.EquipmentDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EquipmentService {
    void saveEquipment(EquipmentDto equipmentDto,
                       Integer idEquipmentDetail,
                       Integer idInvoiceNew,
                       Integer idInvoiceCurrent);

    void delEquipment(Integer idEquipment);

    EquipmentDto getEquipment(Integer idEquipment);

    List<EquipmentDto> getListOfEquipment();

    Page<EquipmentDto> equipmentByPage(int pageSize,
                                       int pageNum,
                                       String sortField,
                                       String sortDir);
}

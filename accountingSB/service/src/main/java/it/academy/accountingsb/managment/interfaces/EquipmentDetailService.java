package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.EquipmentDetailDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EquipmentDetailService {

    void saveEquipmentDetail(EquipmentDetailDto equipmentDetailDto);

    void delEquipmentDetail(Integer idEquipmentDetail);

    EquipmentDetailDto getEquipmentDetail(Integer idEquipmentDetail);

    List<EquipmentDetailDto> getListOfEquipmentDetail();

    Page<EquipmentDetailDto> equipmentDetailByPage(int pageSize,
                                                   int pageNum,
                                                   String sortField,
                                                   String sortDir);
}

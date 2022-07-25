package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.EquipmentDetailDto;
import it.academy.accountingsb.entity.EquipmentDetail;
import org.mapstruct.Mapper;

import javax.xml.validation.Validator;

@Mapper(componentModel = "spring",
        uses = {EquipmentMapper.class,
                Validator.class})
public interface EquipmentDetailMapper {

    EquipmentDetailDto toEquipmentDetailDto(EquipmentDetail equipmentDetail);

    EquipmentDetail toEquipmentDetail(EquipmentDetailDto equipmentDetailDto);
}

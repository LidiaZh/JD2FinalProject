package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.ResponsiblePersonDto;
import it.academy.accountingsb.entity.ResponsiblePerson;
import org.mapstruct.Mapper;

import javax.xml.validation.Validator;

@Mapper(componentModel = "spring",
        uses = {DepartmentMapper.class,
                BranchMapper.class,
                Validator.class})
public interface ResponsiblePersonMapper {
    ResponsiblePersonDto toResPersonDto(ResponsiblePerson responsiblePerson);

    ResponsiblePerson toResPerson(ResponsiblePersonDto responsiblePersonDto);
}

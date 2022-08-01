package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.ResponsiblePersonDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResponsiblePersonService {

    void saveResponsiblePerson(ResponsiblePersonDto responsiblePersonDto,
                               Integer idBranch,
                               Integer idDepartment);

    void delResponsiblePerson(Integer idResponsiblePerson);

    ResponsiblePersonDto getResponsiblePerson(Integer id);

    List<ResponsiblePersonDto> getListOfResponsiblePerson();

    Page<ResponsiblePersonDto> responsiblePersonByPage(int pageSize,
                                                       int pageNum,
                                                       String sortField,
                                                       String sortDir);

}

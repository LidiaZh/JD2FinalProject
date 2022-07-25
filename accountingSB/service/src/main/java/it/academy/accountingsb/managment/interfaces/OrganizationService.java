package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.OrganizationDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrganizationService {

    OrganizationDto saveOrganization(OrganizationDto organizationDto);

    void delOrganization(Integer idOrganization);

    OrganizationDto getOrganization(Integer id);

    List<OrganizationDto> getListOfOrganizationDto();

    Page<OrganizationDto> organizationByPage(int pageSize,
                                             int pageNum,
                                             String sortField,
                                             String sortDir);
}

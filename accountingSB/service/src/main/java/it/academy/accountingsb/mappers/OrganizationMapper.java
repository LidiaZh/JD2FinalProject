package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.OrganizationDto;
import it.academy.accountingsb.entity.Organization;
import org.mapstruct.Mapper;

import javax.xml.validation.Validator;

@Mapper(componentModel = "spring",
        uses = {Validator.class})
public interface OrganizationMapper {
    OrganizationDto toOrganizationDto(Organization organization);

    Organization toOrganization(OrganizationDto organizationDto);
}

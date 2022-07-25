package it.academy.accountingsb.controllers.rest;

import it.academy.accountingsb.dto.OrganizationDto;
import it.academy.accountingsb.managment.interfaces.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static it.academy.accountingsb.constants.Const.ID;

@RestController
@RequestMapping("/rest-organizations")
@RequiredArgsConstructor
public class OrganizationRestController {
    private final OrganizationService organizationService;

    @GetMapping()
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<OrganizationDto> getOrganization() {
        return organizationService.getListOfOrganizationDto();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrganizationDto> getPerson(@PathVariable(ID) Integer id) {
        OrganizationDto organizationDto = organizationService.getOrganization(id);
        if (organizationDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(organizationDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<OrganizationDto> createOrganization(OrganizationDto organizationDto) {
        OrganizationDto newOrganizationDto = organizationService.saveOrganization(organizationDto);
        return new ResponseEntity<>(newOrganizationDto, HttpStatus.CREATED);
    }
}

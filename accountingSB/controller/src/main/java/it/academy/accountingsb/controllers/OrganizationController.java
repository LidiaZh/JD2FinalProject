package it.academy.accountingsb.controllers;

import it.academy.accountingsb.dto.OrganizationDto;
import it.academy.accountingsb.managment.interfaces.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.academy.accountingsb.constants.Constant.*;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public String viewOrganizationPage(Model model) {
        return viewPage(model, FIRST_PAGE, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(PAGE_NUM) int pageNum,
                           @RequestParam(SORT_FIELD) String sortField,
                           @RequestParam(SORT_DIR) String sortDir) {
        Page<OrganizationDto> page = organizationService.organizationByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<OrganizationDto> listOfOrganizations = page.getContent();
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(LIST_OF_ORGANIZATIONS, listOfOrganizations);
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return ORGANIZATION_LIST_HTML;
    }

    @GetMapping("/organization-create/{pageNum}/{sortField}/{sortDir}")
    public String createOrganizationForm(@ModelAttribute(ORGANIZATION) OrganizationDto organizationDto,
                                         @PathVariable(PAGE_NUM) int pageNum,
                                         @PathVariable(SORT_FIELD) String sortField,
                                         @PathVariable(SORT_DIR) String sortDir,
                                         Model model) {
        setPageAttribute(pageNum, sortField, sortDir, model);
        return ORGANIZATION_CREATE_HTML;
    }

    @PostMapping("/organization-create")
    public String createOrganization(@ModelAttribute(ORGANIZATION) @Valid OrganizationDto organizationDto,
                                     BindingResult bindingResult,
                                     int pageNum,
                                     String sortField,
                                     String sortDir,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            setPageAttribute(pageNum, sortField, sortDir, model);
            return ORGANIZATION_CREATE_HTML;
        }
        try {
            organizationService.saveOrganization(organizationDto);
        } catch (Exception ex) {
            model.addAttribute(ERROR_MESSAGE, "Такая организация уже создана");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return ORGANIZATION_CREATE_HTML;
        }
        return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
    }

    @GetMapping("/organization-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteOrganizationForm(@PathVariable(ID) Integer idOrganization,
                                         @PathVariable(PAGE_NUM) int pageNum,
                                         @PathVariable(SORT_FIELD) String sortField,
                                         @PathVariable(SORT_DIR) String sortDir,
                                         Model model) {
        OrganizationDto organizationDto = organizationService.getOrganization(idOrganization);
        model.addAttribute(ORGANIZATION, organizationDto);
        setPageAttribute(pageNum, sortField, sortDir, model);
        return ORGANIZATION_DELETE_HTML;
    }

    @PostMapping("/organization-delete")
    public String deleteOrganization(@RequestParam(ID) Integer idOrganization,
                                     int pageNum,
                                     String sortField,
                                     String sortDir,
                                     Model model) {
        try {
            organizationService.delOrganization(idOrganization);
        } catch (Exception ex) {
            model.addAttribute(ORGANIZATION, organizationService.getOrganization(idOrganization));
            model.addAttribute(ERROR_MESSAGE, "Организация не может быть удалена, так как имеет связи с другими таблицами.");
            return viewPage(model, pageNum, sortField, sortDir);
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/organization-update/{id}/{pageNum}/{sortField}/{sortDir}")
    public String editOrganizationForm(@PathVariable(ID) Integer idOrganization,
                                       @PathVariable(PAGE_NUM) int pageNum,
                                       @PathVariable(SORT_FIELD) String sortField,
                                       @PathVariable(SORT_DIR) String sortDir,
                                       Model model) {
        OrganizationDto organizationDto = organizationService.getOrganization(idOrganization);
        model.addAttribute(ORGANIZATION, organizationDto);
        setPageAttribute(pageNum, sortField, sortDir, model);
        return ORGANIZATION_UPDATE_HTML;
    }

    @PostMapping("/organization-update")
    public String editOrganization(@ModelAttribute(ORGANIZATION) @Valid OrganizationDto organizationDto,
                                   BindingResult bindingResult,
                                   int pageNum,
                                   String sortField,
                                   String sortDir,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, organizationDto.getId());
            model.addAttribute(ORGANIZATION, organizationDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            return ORGANIZATION_UPDATE_HTML;
        }
        try {
            organizationService.saveOrganization(organizationDto);
        } catch (Exception ex) {
            model.addAttribute(ID, organizationDto.getId());
            model.addAttribute(ORGANIZATION, organizationDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            model.addAttribute(ERROR_MESSAGE, "Такая организация уже создана.");
            return ORGANIZATION_UPDATE_HTML;
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    private void setPageAttribute(int pageNum,
                                  String sortField,
                                  String sortDir,
                                  Model model) {
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(SORT_FIELD, sortField);
        model.addAttribute(SORT_DIR, sortDir);
    }

}

package it.academy.accountingsb.controllers;

import it.academy.accountingsb.dto.ResponsiblePersonDto;
import it.academy.accountingsb.managment.interfaces.BranchService;
import it.academy.accountingsb.managment.interfaces.DepartmentService;
import it.academy.accountingsb.managment.interfaces.ResponsiblePersonService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.academy.accountingsb.constants.Constant.*;

@Controller
@RequestMapping("/resPersons")
public class ResponsiblePersonController {


    private final ResponsiblePersonService responsiblePersonService;
    private final BranchService branchService;
    private final DepartmentService departmentService;

    public ResponsiblePersonController(ResponsiblePersonService responsiblePersonService,
                                       BranchService branchService,
                                       DepartmentService departmentService) {
        this.responsiblePersonService = responsiblePersonService;
        this.branchService = branchService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String viewResponsiblePersonPage(Model model) {
        return viewPage(model, FIRST_PAGE, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(PAGE_NUM) int pageNum,
                           @RequestParam(SORT_FIELD) String sortField,
                           @RequestParam(SORT_DIR) String sortDir) {
        Page<ResponsiblePersonDto> page = responsiblePersonService.responsiblePersonByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<ResponsiblePersonDto> listOfResponsiblePersons = page.getContent();
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, listOfResponsiblePersons);
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return RES_PERSON_LIST_HTML;
    }

    @GetMapping("/resPerson-create/{pageNum}/{sortField}/{sortDir}")
    public String createResponsiblePersonForm(@ModelAttribute(RESPONSIBLE_PERSON) ResponsiblePersonDto responsiblePersonDto,
                                              @PathVariable(PAGE_NUM) int pageNum,
                                              @PathVariable(SORT_FIELD) String sortField,
                                              @PathVariable(SORT_DIR) String sortDir,
                                              Model model) {
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(LIST_OF_DEPARTMENTS, departmentService.getListOfDepartmentDto());
        model.addAttribute(LIST_OF_BRANCHES, branchService.getListOfBranchDto());
        return RES_PERSON_CREATE_HTML;
    }

    @PostMapping("/resPerson-create")
    public String createResponsiblePerson(@ModelAttribute(RESPONSIBLE_PERSON) @Valid ResponsiblePersonDto responsiblePersonDto,
                                          BindingResult bindingResult,
                                          @RequestParam(BRANCH_DOT_ID) Integer idBranch,
                                          @RequestParam(DEPARTMENT_DOT_ID) Integer idDepartment,
                                          int pageNum,
                                          String sortField,
                                          String sortDir,
                                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(LIST_OF_DEPARTMENTS, departmentService.getListOfDepartmentDto());
            model.addAttribute(LIST_OF_BRANCHES, branchService.getListOfBranchDto());
            model.addAttribute(BRANCH_ID,idBranch);
            model.addAttribute(DEPARTMENT_ID,idDepartment);
            setPageAttribute(pageNum, sortField, sortDir, model);
            return RES_PERSON_CREATE_HTML;
        }
        try {
            responsiblePersonService.saveResponsiblePerson(responsiblePersonDto, idBranch, idDepartment);
        } catch (Exception ex) {
            model.addAttribute(RESPONSIBLE_PERSON, responsiblePersonDto);
            model.addAttribute(BRANCH, branchService.getBranch(idBranch));
            model.addAttribute(DEPARTMENT, departmentService.getDepartment(idDepartment));
            model.addAttribute(ERROR_MESSAGE, "МОЛ не был добавлен в данный отдел.");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
        }
        return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
    }

    @GetMapping("/resPerson-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteResponsiblePersonForm(@PathVariable(ID) Integer idResponsiblePerson,
                                              @PathVariable(PAGE_NUM) int pageNum,
                                              @PathVariable(SORT_FIELD) String sortField,
                                              @PathVariable(SORT_DIR) String sortDir,
                                              Model model) {
        model.addAttribute(RESPONSIBLE_PERSON, responsiblePersonService.getResponsiblePerson(idResponsiblePerson));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return RES_PERSON_DELETE_HTML;
    }

    @PostMapping("/resPerson-delete")
    public String deleteResponsiblePerson(@RequestParam(ID) Integer idResponsiblePerson,
                                          int pageNum,
                                          String sortField,
                                          String sortDir,
                                          Model model) {
        try {
            responsiblePersonService.delResponsiblePerson(idResponsiblePerson);
        } catch (Exception ex) {
            model.addAttribute(ID, idResponsiblePerson);
            model.addAttribute(RESPONSIBLE_PERSON, responsiblePersonService.getResponsiblePerson(idResponsiblePerson));
            model.addAttribute(ERROR_MESSAGE, "МОЛ не может быть удален, так как имеет связи с другими таблицами.");
            return viewPage(model, pageNum, sortField, sortDir);
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/resPerson-update/{id}/{pageNum}/{sortField}/{sortDir}/{formAction}")
    public String editResponsiblePersonForm(@PathVariable(ID) Integer idResponsiblePerson,
                                            @PathVariable(PAGE_NUM) int pageNum,
                                            @PathVariable(SORT_FIELD) String sortField,
                                            @PathVariable(SORT_DIR) String sortDir,
                                            @PathVariable(FORM_ACTION) String formAction,
                                            Model model) {
        model.addAttribute(RESPONSIBLE_PERSON, responsiblePersonService.getResponsiblePerson(idResponsiblePerson));
        model.addAttribute(FORM_ACTION, formAction);
        model.addAttribute(LIST_OF_DEPARTMENTS, departmentService.getListOfDepartmentDto());
        model.addAttribute(LIST_OF_BRANCHES, branchService.getListOfBranchDto());
        setPageAttribute(pageNum, sortField, sortDir, model);
        return RES_PERSON_UPDATE_HTML;
    }

    @PostMapping("/resPerson-update")
    public String editResponsiblePerson(@ModelAttribute(RESPONSIBLE_PERSON) @Valid ResponsiblePersonDto responsiblePersonDto,
                                        BindingResult bindingResult,
                                        @RequestParam(BRANCH_DOT_ID) Integer idBranch,
                                        @RequestParam(DEPARTMENT_DOT_ID) Integer idDepartment,
                                        int pageNum,
                                        String sortField,
                                        String sortDir,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, responsiblePersonDto.getId());
            model.addAttribute(RESPONSIBLE_PERSON, responsiblePersonDto);
            model.addAttribute(BRANCH_ID, responsiblePersonDto.getBranch().getId());
            model.addAttribute(FORM_ACTION, "change");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return RES_PERSON_UPDATE_HTML;
        }
        try {
            responsiblePersonService.saveResponsiblePerson(responsiblePersonDto, idBranch, idDepartment);
        } catch (Exception ex) {
            model.addAttribute(RESPONSIBLE_PERSON, responsiblePersonDto);
            model.addAttribute(ERROR_MESSAGE, "Информация о МОЛ не была отредактирована.");
            return viewPage(model, pageNum, sortField, sortDir);
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

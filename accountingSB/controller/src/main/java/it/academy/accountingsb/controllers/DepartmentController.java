package it.academy.accountingsb.controllers;

import it.academy.accountingsb.dto.DepartmentDto;
import it.academy.accountingsb.managment.interfaces.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.academy.accountingsb.constants.Const.*;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String viewDepartmentPage(Model model) {
        return viewPage(model, 1, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(PAGE_NUM) int pageNum,
                           @RequestParam(SORT_FIELD) String sortField,
                           @RequestParam(SORT_DIR) String sortDir) {
        Page<DepartmentDto> page = departmentService.departmentByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<DepartmentDto> listOfDepartments = page.getContent();
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(LIST_OF_DEPARTMENTS, listOfDepartments);
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return DEPARTMENT_LIST_HTML;
    }

    @GetMapping("/department-create/{pageNum}/{sortField}/{sortDir}")
    public String createDepartmentForm(@ModelAttribute(DEPARTMENT) DepartmentDto departmentDto,
                                       @PathVariable(PAGE_NUM) int pageNum,
                                       @PathVariable(SORT_FIELD) String sortField,
                                       @PathVariable(SORT_DIR) String sortDir,
                                       Model model) {
        setPageAttribute(pageNum, sortField, sortDir, model);
        return DEPARTMENT_CREATE_HTML;
    }

    @PostMapping("/department-create")
    public String createDepartment(@ModelAttribute(DEPARTMENT) @Valid DepartmentDto departmentDto,
                                   BindingResult bindingResult,
                                   int pageNum,
                                   String sortField,
                                   String sortDir,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            setPageAttribute(pageNum, sortField, sortDir, model);

            return DEPARTMENT_CREATE_HTML;
        }
        try {
            departmentService.saveDepartment(departmentDto);
        } catch (Exception ex) {
            model.addAttribute(ERROR_MESSAGE, "Такой отдел уже создан");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return DEPARTMENT_CREATE_HTML;
        }
        return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
    }

    @GetMapping("/department-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteDepartmentForm(@PathVariable(ID) Integer idDepartment,
                                       @PathVariable(PAGE_NUM) int pageNum,
                                       @PathVariable(SORT_FIELD) String sortField,
                                       @PathVariable(SORT_DIR) String sortDir,
                                       Model model) {
        DepartmentDto departmentDto = departmentService.getDepartment(idDepartment);
        model.addAttribute(DEPARTMENT, departmentDto);
        setPageAttribute(pageNum, sortField, sortDir, model);
        return DEPARTMENT_DELETE_HTML;
    }

    @PostMapping("/department-delete")
    public String createDepartment(@RequestParam(ID) Integer idDepartment,
                                   int pageNum,
                                   String sortField,
                                   String sortDir,
                                   Model model) {
        try {
            departmentService.delDepartment(idDepartment);
        } catch (Exception ex) {
            model.addAttribute(DEPARTMENT, departmentService.getDepartment(idDepartment));
            model.addAttribute(ERROR_MESSAGE, "не может быть удален, так как имеет связи с другими таблицами.");
            return viewPage(model, pageNum, sortField, sortDir);
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/department-update/{id}/{pageNum}/{sortField}/{sortDir}")
    public String editDepartmentForm(@PathVariable(ID) Integer idDepartment,
                                     @PathVariable(PAGE_NUM) int pageNum,
                                     @PathVariable(SORT_FIELD) String sortField,
                                     @PathVariable(SORT_DIR) String sortDir,
                                     Model model) {
        model.addAttribute(DEPARTMENT, departmentService.getDepartment(idDepartment));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return DEPARTMENT_UPDATE_HTML;
    }

    @PostMapping("/department-update")
    public String editDepartment(@ModelAttribute(DEPARTMENT) @Valid DepartmentDto departmentDto,
                                 BindingResult bindingResult,
                                 int pageNum,
                                 String sortField,
                                 String sortDir,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, departmentDto.getId());
            model.addAttribute(DEPARTMENT, departmentDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            return DEPARTMENT_UPDATE_HTML;
        }
        try {
            departmentService.saveDepartment(departmentDto);
        } catch (Exception ex) {
            model.addAttribute(ID, departmentDto.getId());
            model.addAttribute(DEPARTMENT, departmentDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            model.addAttribute(ERROR_MESSAGE, "Такой отдел уже создан.");
            return DEPARTMENT_UPDATE_HTML;
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

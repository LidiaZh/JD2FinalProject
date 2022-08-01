package it.academy.accountingsb.controllers;

import it.academy.accountingsb.managment.interfaces.BranchService;
import it.academy.accountingsb.managment.interfaces.DepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static it.academy.accountingsb.constants.Constant.*;

@Controller
@RequestMapping("branches_departments")
public class BranchAndDepartmentController {

    private final BranchService branchService;
    private final DepartmentService departmentService;

    public BranchAndDepartmentController(BranchService branchService,
                                         DepartmentService departmentService) {
        this.branchService = branchService;
        this.departmentService = departmentService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public String viewBranchAndDepartmentPage(Model model) {
        model.addAttribute(LIST_OF_BRANCHES_DTO, branchService.getListOfBranchDto());
        return DEPARTMENT_BRANCH_HTML;
    }

    @PostMapping("/delete")
    private String deleteDepartment(@RequestParam(BRANCH_ID) Integer idBranch,
                                    @RequestParam(DEPARTMENT_ID) Integer idDepartment) {
        branchService.delDepartmentFromBranch(idBranch, idDepartment);
        return REDIRECT_BRANCHES_DEPARTMENTS;
    }

    @GetMapping("/add/{idBranch}")
    private String addDepartmentInBranch(@PathVariable(BRANCH_ID) Integer idBranch,
                                         Model model) {
        model.addAttribute(BRANCH_ID, idBranch);
        model.addAttribute(LIST_OF_ALL_DEPARTMENTS, departmentService.getListOfDepartmentDto());
        return BRANCH_DEPARTMENT_FORM_HTML;
    }

    @PostMapping("/add")
    private String addDepartmentInBranch(@RequestParam(value = BOX_NAME_OF_DEP, required = false) String[] departments,
                                         @RequestParam(BRANCH_ID) Integer idBranch) {
        if (departments != null) {
            branchService.addDepartmentInBranch(idBranch, departments);
        }
        return REDIRECT_BRANCHES_DEPARTMENTS;
    }
}

package it.academy.accountingsb.controllers;

import it.academy.accountingsb.constants.Constant;
import it.academy.accountingsb.dto.BranchDto;
import it.academy.accountingsb.managment.interfaces.BranchService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.academy.accountingsb.constants.Constant.*;

@Controller
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public String viewBranchPage(Model model) {
        return viewPage(model, FIRST_PAGE, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(PAGE_NUM) int pageNum,
                           @RequestParam(SORT_FIELD) String sortField,
                           @RequestParam(SORT_DIR) String sortDir) {
        Page<BranchDto> page = branchService.branchByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<BranchDto> listOfBranches = page.getContent();
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(LIST_OF_BRANCHES, listOfBranches);
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return BRANCH_LIST_HTML;
    }

    @GetMapping("/branch-create/{pageNum}/{sortField}/{sortDir}")
    public String createBranchForm(@ModelAttribute(BRANCH) BranchDto branchDto,
                                   @PathVariable(PAGE_NUM) int pageNum,
                                   @PathVariable(SORT_FIELD) String sortField,
                                   @PathVariable(SORT_DIR) String sortDir,
                                   Model model) {
        setPageAttribute(pageNum, sortField, sortDir, model);
        return BRANCH_CREATE_HTML;
    }

    @PostMapping("/branch-create")
    public String createBranch(@ModelAttribute(BRANCH) @Valid BranchDto branchDto,
                               BindingResult bindingResult,
                               int pageNum,
                               String sortField,
                               String sortDir,
                               Model model) {
        if (bindingResult.hasErrors()) {
            setPageAttribute(pageNum, sortField, sortDir, model);
            return BRANCH_CREATE_HTML;
        }
        try {
            branchService.saveBranch(branchDto);
        } catch (Exception ex) {
            model.addAttribute(ERROR_MESSAGE, "Такой филиал уже создан");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return BRANCH_CREATE_HTML;
        }
        return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
    }

    @GetMapping("/branch-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteBranchForm(@PathVariable(ID) Integer idBranch,
                                   @PathVariable(PAGE_NUM) int pageNum,
                                   @PathVariable(SORT_FIELD) String sortField,
                                   @PathVariable(SORT_DIR) String sortDir,
                                   Model model) {
        BranchDto branchDto = branchService.getBranch(idBranch);
        model.addAttribute(Constant.BRANCH, branchDto);
        setPageAttribute(pageNum, sortField, sortDir, model);
        return BRANCH_DELETE_HTML;
    }

    @PostMapping("/branch-delete")
    public String deleteBranch(@RequestParam(ID) Integer idBranch,
                               int pageNum,
                               String sortField,
                               String sortDir,
                               Model model) {
        try {
            branchService.delBranch(idBranch);
        } catch (Exception ex) {
            model.addAttribute(BRANCH, branchService.getBranch(idBranch));
            model.addAttribute(ERROR_MESSAGE, "не может быть удален, так как имеет связи с другими таблицами.");
            return viewPage(model, pageNum, sortField, sortDir);
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/branch-update/{id}/{pageNum}/{sortField}/{sortDir}")
    public String editBranchForm(@PathVariable(ID) Integer idBranch,
                                 @PathVariable(PAGE_NUM) int pageNum,
                                 @PathVariable(SORT_FIELD) String sortField,
                                 @PathVariable(SORT_DIR) String sortDir,
                                 Model model) {
        model.addAttribute(BRANCH, branchService.getBranch(idBranch));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return BRANCH_UPDATE_HTML;
    }

    @PostMapping("/branch-update")
    public String editBranch(@ModelAttribute(BRANCH) @Valid BranchDto branchDto,
                             BindingResult bindingResult,
                             int pageNum,
                             String sortField,
                             String sortDir,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, branchDto.getId());
            model.addAttribute(BRANCH, branchDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            return BRANCH_UPDATE_HTML;
        }
        try {
            branchService.saveBranch(branchDto);
        } catch (Exception ex) {
            model.addAttribute(ID, branchDto.getId());
            model.addAttribute(BRANCH, branchDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            model.addAttribute(ERROR_MESSAGE, "Такой филиал уже создан.");
            return BRANCH_UPDATE_HTML;
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

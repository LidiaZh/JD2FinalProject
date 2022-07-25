package it.academy.accountingsb.controllers;

import it.academy.accountingsb.dto.EquipmentDetailDto;
import it.academy.accountingsb.managment.interfaces.EquipmentDetailService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static it.academy.accountingsb.constants.Const.*;

@Controller
@RequestMapping("/equipmentDetails")
public class EquipmentDetailController {

    private final EquipmentDetailService equipmentDetailService;

    public EquipmentDetailController(EquipmentDetailService equipmentDetailService) {
        this.equipmentDetailService = equipmentDetailService;
    }

    @GetMapping
    public String viewEquipmentDetailPage(Model model) {
        return viewPage(model, FIRST_PAGE, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(PAGE_NUM) int pageNum,
                           @RequestParam(SORT_FIELD) String sortField,
                           @RequestParam(SORT_DIR) String sortDir) {
        Page<EquipmentDetailDto> page = equipmentDetailService.equipmentDetailByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<EquipmentDetailDto> listOfEquipmentDetails = page.getContent();
        model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, listOfEquipmentDetails);
        setPageAttribute(pageNum, sortField, sortDir, model);
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return EQUIPMENT_DETAIL_LIST_HTML;
    }

    @GetMapping("/equipmentDetail-create/{pageNum}/{sortField}/{sortDir}")
    public String createEquipmentDetailForm(@ModelAttribute(EQUIPMENT_DETAIL) EquipmentDetailDto equipmentDetailDto,
                                            @PathVariable(PAGE_NUM) int pageNum,
                                            @PathVariable(SORT_FIELD) String sortField,
                                            @PathVariable(SORT_DIR) String sortDir,
                                            Model model) {
        setPageAttribute(pageNum, sortField, sortDir, model);
        return EQUIPMENT_DETAIL_CREATE_HTML;
    }

    @PostMapping("/equipmentDetail-create")
    public String createEquipmentDetail(@ModelAttribute(EQUIPMENT_DETAIL) @Valid EquipmentDetailDto equipmentDetailDto,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            return EQUIPMENT_DETAIL_CREATE_HTML;
        }
        try {
            equipmentDetailService.saveEquipmentDetail(equipmentDetailDto);
        } catch (Exception ex) {
            model.addAttribute(ERROR_MESSAGE, "Такой тип оборудования уже создан");
            return EQUIPMENT_DETAIL_CREATE_HTML;
        }
        return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
    }

    @GetMapping("/equipmentDetail-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteEquipmentDetailForm(@PathVariable(ID) Integer idEquipmentDetail,
                                            @PathVariable(PAGE_NUM) int pageNum,
                                            @PathVariable(SORT_FIELD) String sortField,
                                            @PathVariable(SORT_DIR) String sortDir,
                                            Model model) {
        model.addAttribute(EQUIPMENT_DETAIL, equipmentDetailService.getEquipmentDetail(idEquipmentDetail));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return EQUIPMENT_DETAIL_DELETE_HTML;
    }

    @PostMapping("/equipmentDetail-delete")
    public String deleteEquipmentDetail(@RequestParam(ID) Integer idEquipmentDetail,
                                        int pageNum,
                                        String sortField,
                                        String sortDir,
                                        Model model) {
        try {
            equipmentDetailService.delEquipmentDetail(idEquipmentDetail);
        } catch (Exception ex) {
            model.addAttribute(EQUIPMENT_DETAIL, equipmentDetailService.getEquipmentDetail(idEquipmentDetail));
            model.addAttribute(ERROR_MESSAGE, "не может быть удален, так как имеет связи с другими таблицами.");
            return viewPage(model, pageNum, sortField, sortDir);
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/equipmentDetail-update/{id}/{pageNum}/{sortField}/{sortDir}")
    public String editEquipmentDetailForm(@PathVariable(ID) Integer idEquipmentDetail,
                                          @PathVariable(PAGE_NUM) int pageNum,
                                          @PathVariable(SORT_FIELD) String sortField,
                                          @PathVariable(SORT_DIR) String sortDir,
                                          Model model) {
        model.addAttribute(EQUIPMENT_DETAIL, equipmentDetailService.getEquipmentDetail(idEquipmentDetail));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return EQUIPMENT_DETAIL_UPDATE_HTML;
    }

    @PostMapping("/equipmentDetail-update")
    public String editEquipmentDetail(@ModelAttribute(EQUIPMENT_DETAIL) @Valid EquipmentDetailDto equipmentDetailDto,
                                      BindingResult bindingResult,
                                      int pageNum,
                                      String sortField,
                                      String sortDir,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, equipmentDetailDto.getId());
            model.addAttribute(EQUIPMENT_DETAIL, equipmentDetailDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            return EQUIPMENT_DETAIL_UPDATE_HTML;
        }
        try {
            equipmentDetailService.saveEquipmentDetail(equipmentDetailDto);
        } catch (Exception ex) {
            model.addAttribute(ID, equipmentDetailDto.getId());
            model.addAttribute(EQUIPMENT_DETAIL, equipmentDetailDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            model.addAttribute(ERROR_MESSAGE, "Такой тип оборудования уже создан.");
            return EQUIPMENT_DETAIL_UPDATE_HTML;
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

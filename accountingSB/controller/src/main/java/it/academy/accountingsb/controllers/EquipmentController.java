package it.academy.accountingsb.controllers;

import it.academy.accountingsb.constants.Constant;
import it.academy.accountingsb.dto.EquipmentDto;
import it.academy.accountingsb.managment.interfaces.EquipmentDetailService;
import it.academy.accountingsb.managment.interfaces.EquipmentService;
import it.academy.accountingsb.managment.interfaces.InvoiceService;
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
@RequestMapping("/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final InvoiceService invoiceService;
    private final EquipmentDetailService equipmentDetailService;
    private final ResponsiblePersonService resPersonService;

    public EquipmentController(EquipmentService equipmentService,
                               InvoiceService invoiceService,
                               EquipmentDetailService equipmentDetailService,
                               ResponsiblePersonService resPersonService) {
        this.equipmentService = equipmentService;
        this.invoiceService = invoiceService;
        this.equipmentDetailService = equipmentDetailService;
        this.resPersonService = resPersonService;
    }

    @GetMapping
    public String viewEquipmentPage(Model model) {
        return viewPageOfEquipments(model, FIRST_PAGE, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPageOfEquipments(Model model,
                                       @PathVariable(PAGE_NUM) int pageNum,
                                       @RequestParam(SORT_FIELD) String sortField,
                                       @RequestParam(SORT_DIR) String sortDir) {
        Page<EquipmentDto> page = equipmentService.equipmentByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<EquipmentDto> listOfEquipments = page.getContent();
        model.addAttribute(LIST_OF_EQUIPMENTS, listOfEquipments);
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return EQUIPMENT_LIST_HTML;
    }

    @GetMapping("/equipment-create/{pageNum}/{sortField}/{sortDir}")
    public String createEquipmentForm(@ModelAttribute(EQUIPMENT) EquipmentDto equipmentDto,
                                      @PathVariable(PAGE_NUM) int pageNum,
                                      @PathVariable(SORT_FIELD) String sortField,
                                      @PathVariable(SORT_DIR) String sortDir,
                                      Model model) {
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(LIST_OF_INVOICES, invoiceService.getListOfInvoiceDto());
        model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, equipmentDetailService.getListOfEquipmentDetail());
        model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, resPersonService.getListOfResponsiblePerson());
        return EQUIPMENT_CREATE_HTML;
    }

    @PostMapping("/equipment-create")
    public String createEquipment(@ModelAttribute(EQUIPMENT) @Valid EquipmentDto equipmentDto,
                                  BindingResult bindingResult,
                                  @RequestParam(EQUIPMENT_DETAIL_DOT_ID) Integer idEquipmentDetail,
                                  @RequestParam(INVOICE_LAST_ID) Integer idInvoiceNew,
                                  int pageNum,
                                  String sortField,
                                  String sortDir,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(LIST_OF_INVOICES, invoiceService.getListOfInvoiceDto());
            model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, equipmentDetailService.getListOfEquipmentDetail());
            model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, resPersonService.getListOfResponsiblePerson());
            setPageAttribute(pageNum, sortField, sortDir, model);
            return EQUIPMENT_CREATE_HTML;
        }
        try {
            equipmentService.saveEquipment(equipmentDto, idEquipmentDetail, idInvoiceNew, null);
        } catch (Exception ex) {
            model.addAttribute(LIST_OF_INVOICES, invoiceService.getListOfInvoiceDto());
            model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, equipmentDetailService.getListOfEquipmentDetail());
            model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, resPersonService.getListOfResponsiblePerson());
            model.addAttribute(ERROR_MESSAGE, "Такое оборудование уже создано");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return EQUIPMENT_CREATE_HTML;
        }
        return viewPageOfEquipments(model, FIRST_PAGE, ID, DESC_SORT);
    }

    @GetMapping("/equipment-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteEquipmentForm(@PathVariable(ID) Integer idEquipment,
                                      @PathVariable(PAGE_NUM) int pageNum,
                                      @PathVariable(SORT_FIELD) String sortField,
                                      @PathVariable(SORT_DIR) String sortDir,
                                      Model model) {
        model.addAttribute(EQUIPMENT, equipmentService.getEquipment(idEquipment));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return EQUIPMENT_DELETE_HTML;
    }

    @PostMapping("/equipment-delete")
    public String deleteEquipment(@RequestParam(ID) Integer idEquipment,
                                  int pageNum,
                                  String sortField,
                                  String sortDir,
                                  Model model) {
        try {
            equipmentService.delEquipment(idEquipment);
        } catch (Exception ex) {
            model.addAttribute(EQUIPMENT, equipmentService.getEquipment(idEquipment));
            model.addAttribute(ERROR_MESSAGE, "Оборудование не может быть удалено, так как имеет связи с другими таблицами.");
            return viewPageOfEquipments(model, FIRST_PAGE, ID, DESC_SORT);
        }
        return viewPageOfEquipments(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/equipment-update/{id}/{idInvoiceLast}/{pageNum}/{sortField}/{sortDir}")
    public String editEquipmentForm(@PathVariable(ID) Integer idEquipment,
                                    @PathVariable(PAGE_NUM) int pageNum,
                                    @PathVariable(SORT_FIELD) String sortField,
                                    @PathVariable(SORT_DIR) String sortDir,
                                    @PathVariable(INVOICE_LAST_ID) Integer idInvoiceCurrent,
                                    Model model) {
        model.addAttribute(EQUIPMENT, equipmentService.getEquipment(idEquipment));
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(LIST_OF_INVOICES, invoiceService.getListOfInvoiceDto());
        model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, equipmentDetailService.getListOfEquipmentDetail());
        model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, resPersonService.getListOfResponsiblePerson());
        model.addAttribute(Constant.INVOICE_CURRENT_ID, idInvoiceCurrent);
        return EQUIPMENT_UPDATE_HTML;
    }

    @PostMapping("/equipment-update")
    public String editEquipment(@ModelAttribute(EQUIPMENT) @Valid EquipmentDto equipmentDto,
                                BindingResult bindingResult,
                                @RequestParam(EQUIPMENT_DETAIL_DOT_ID) Integer idEquipmentDetail,
                                @RequestParam(INVOICE_LAST_ID) Integer idInvoiceNew,
                                @RequestParam(INVOICE_CURRENT_ID) Integer idInvoiceCurrent,
                                int pageNum,
                                String sortField,
                                String sortDir,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, equipmentDto.getId());
            model.addAttribute(EQUIPMENT, equipmentDto);
            model.addAttribute(INVOICE_CURRENT_ID, idInvoiceCurrent);
            model.addAttribute(LIST_OF_INVOICES, invoiceService.getListOfInvoiceDto());
            model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, equipmentDetailService.getListOfEquipmentDetail());
            model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, resPersonService.getListOfResponsiblePerson());
            setPageAttribute(pageNum, sortField, sortDir, model);
            return EQUIPMENT_UPDATE_HTML;
        }
        try {
            equipmentService.saveEquipment(equipmentDto, idEquipmentDetail, idInvoiceNew, idInvoiceCurrent);
        } catch (Exception ex) {
            model.addAttribute(ID, equipmentDto.getId());
            model.addAttribute(EQUIPMENT, equipmentDto);
            setPageAttribute(pageNum, sortField, sortDir, model);
            model.addAttribute(LIST_OF_INVOICES, invoiceService.getListOfInvoiceDto());
            model.addAttribute(LIST_OF_EQUIPMENT_DETAILS, equipmentDetailService.getListOfEquipmentDetail());
            model.addAttribute(LIST_OF_RESPONSIBLE_PERSONS, resPersonService.getListOfResponsiblePerson());
            model.addAttribute(ERROR_MESSAGE, "Такое оборудование уже создано.");
            return EQUIPMENT_UPDATE_HTML;
        }
        return viewPageOfEquipments(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/equipment-info/{id}/{pageNum}/{sortField}/{sortDir}")
    public String editEquipmentForm(@PathVariable(ID) Integer idEquipment,
                                    @PathVariable(PAGE_NUM) int pageNum,
                                    @PathVariable(SORT_FIELD) String sortField,
                                    @PathVariable(SORT_DIR) String sortDir,
                                    Model model) {
        model.addAttribute(EQUIPMENT, equipmentService.getEquipment(idEquipment));
        model.addAttribute(LIST_OF_INVOICES1, invoiceService.getListOfInvoiceDto());
        setPageAttribute(pageNum, sortField, sortDir, model);
        return EQUIPMENT_INFO_HTML;
    }

    @GetMapping("/writeInvoice")
    public String viewEquipmentPageForInvoice(Model model,
                                              @ModelAttribute(INVOICE_ID) Integer idInvoice) {
        return viewPageOfEquipmentsToInvoice(model, FIRST_PAGE, ID, ASC_SORT, idInvoice);
    }

    @GetMapping("/pages/{pageNum}")
    public String viewPageOfEquipmentsToInvoice(Model model,
                                                @PathVariable(PAGE_NUM) int pageNum,
                                                @RequestParam(SORT_FIELD) String sortField,
                                                @RequestParam(SORT_DIR) String sortDir,
                                                @RequestParam(INVOICE_ID) Integer idInvoice) {
        Page<EquipmentDto> page = equipmentService.equipmentByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<EquipmentDto> listOfEquipments = page.getContent();
        model.addAttribute(LIST_OF_EQUIPMENTS, listOfEquipments);
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(INVOICE_ID, idInvoice);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return EQUIPMENT_TO_INVOICE_LIST_HTML;
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


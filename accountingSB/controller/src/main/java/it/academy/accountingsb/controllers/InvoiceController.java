package it.academy.accountingsb.controllers;

import it.academy.accountingsb.dto.InvoiceDto;
import it.academy.accountingsb.managment.interfaces.BranchService;
import it.academy.accountingsb.managment.interfaces.InvoiceService;
import it.academy.accountingsb.managment.interfaces.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static it.academy.accountingsb.constants.Const.*;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final OrganizationService organizationService;
    private final BranchService branchService;

    public InvoiceController(InvoiceService invoiceService, OrganizationService organizationService, BranchService branchService) {
        this.invoiceService = invoiceService;
        this.organizationService = organizationService;
        this.branchService = branchService;
    }

    @GetMapping
    public String viewInvoicePage(Model model) {
        return viewPage(model, FIRST_PAGE, ID, ASC_SORT);
    }

    @GetMapping("/page/{pageNum}")
    public String viewPage(Model model,
                           @PathVariable(PAGE_NUM) int pageNum,
                           @RequestParam(SORT_FIELD) String sortField,
                           @RequestParam(SORT_DIR) String sortDir) {
        Page<InvoiceDto> page = invoiceService.invoiceByPage(PAGE_SIZE_NUMBER, pageNum, sortField, sortDir);
        List<InvoiceDto> listOfInvoices = page.getContent();
        setPageAttribute(pageNum, sortField, sortDir, model);
        model.addAttribute(TOTAL_PAGES, page.getTotalPages());
        model.addAttribute(TOTAL_ITEMS, page.getTotalElements());
        model.addAttribute(LIST_OF_INVOICES, listOfInvoices);
        model.addAttribute(PAGE_SIZE, PAGE_SIZE_NUMBER);
        model.addAttribute(REVERSE_SORT_DIR, sortDir.equals(ASC_SORT) ? DESC_SORT : ASC_SORT);
        return INVOICE_LIST_HTML;
    }

    @GetMapping("/invoice-create/{pageNum}/{sortField}/{sortDir}/{formAction}")
    public String createInvoiceForm(@ModelAttribute(INVOICE) InvoiceDto invoiceDto,
                                    @PathVariable(PAGE_NUM) int pageNum,
                                    @PathVariable(SORT_FIELD) String sortField,
                                    @PathVariable(SORT_DIR) String sortDir,
                                    @PathVariable(FORM_ACTION) String formAction,
                                    Model model) {
        model.addAttribute(FORM_ACTION, formAction);
        model.addAttribute(LOCAL_DATE, LocalDate.now());
        model.addAttribute(LIST_OF_ORGANIZATIONS, organizationService.getListOfOrganizationDto());
        model.addAttribute(LIST_OF_BRANCHES, branchService.getListOfBranchDto());
        setPageAttribute(pageNum, sortField, sortDir, model);
        return INVOICE_CREATE_HTML;
    }

    @PostMapping("/invoice-create")
    public String createInvoice(@ModelAttribute(INVOICE) @Valid InvoiceDto invoiceDto,
                                BindingResult bindingResult,
                                @RequestParam(SUPPLIER_DOT_ID) Integer idSupplier,
                                @RequestParam(RECEIVER_DOT_ID) Integer idReceiver,
                                @RequestParam(FORM_ACTION) String formAction,
                                RedirectAttributes redirectAttributes,
                                int pageNum,
                                String sortField,
                                String sortDir,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(FORM_ACTION, formAction);
            model.addAttribute(LOCAL_DATE, LocalDate.now());
            model.addAttribute(LIST_OF_ORGANIZATIONS, organizationService.getListOfOrganizationDto());
            model.addAttribute(LIST_OF_BRANCHES, branchService.getListOfBranchDto());
            setPageAttribute(pageNum, sortField, sortDir, model);
            return INVOICE_CREATE_HTML;
        }
        try {
            Integer idInvoice = invoiceService.saveInvoice(invoiceDto, idSupplier, idReceiver).getId();
            if (formAction.equals(ADD_INVOICE)) {
                return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
            } else {
                redirectAttributes.addFlashAttribute(INVOICE_ID, idInvoice);
                return REDIRECT_EQUIPMENTS_WRITE_INVOICE;
            }
        } catch (Exception ex) {
            model.addAttribute(ERROR_MESSAGE, "Такая накладная уже создана.");
            model.addAttribute(FORM_ACTION, formAction);
            model.addAttribute(LOCAL_DATE, LocalDate.now());
            model.addAttribute(LIST_OF_ORGANIZATIONS, organizationService.getListOfOrganizationDto());
            model.addAttribute(LIST_OF_BRANCHES, branchService.getListOfBranchDto());
            setPageAttribute(pageNum, sortField, sortDir, model);
            return INVOICE_CREATE_HTML;
        }
    }

    @GetMapping("/invoice-delete/{id}/{pageNum}/{sortField}/{sortDir}")
    public String deleteInvoiceForm(@PathVariable(ID) Integer idInvoice,
                                    @PathVariable(PAGE_NUM) int pageNum,
                                    @PathVariable(SORT_FIELD) String sortField,
                                    @PathVariable(SORT_DIR) String sortDir,
                                    Model model) {
        model.addAttribute(INVOICE, invoiceService.getInvoice(idInvoice));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return INVOICE_DELETE_HTML;
    }

    @PostMapping("/invoice-delete")
    public String deleteInvoice(@RequestParam(ID) Integer idInvoice,
                                int pageNum,
                                String sortField,
                                String sortDir,
                                Model model) {
        try {
            invoiceService.delInvoice(idInvoice);
        } catch (Exception ex) {
            model.addAttribute(INVOICE, invoiceService.getInvoice(idInvoice));
            model.addAttribute(ERROR_MESSAGE, "Накладаная не может быть удалена, так как имеет связи с другими таблицами.");
            return viewPage(model, pageNum, sortField, sortDir);
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/invoice-update/{id}/{pageNum}/{sortField}/{sortDir}")
    public String editInvoiceForm(@PathVariable(ID) Integer idInvoice,
                                  @PathVariable(PAGE_NUM) int pageNum,
                                  @PathVariable(SORT_FIELD) String sortField,
                                  @PathVariable(SORT_DIR) String sortDir,
                                  Model model) {
        model.addAttribute(INVOICE, invoiceService.getInvoice(idInvoice));
        model.addAttribute(LIST_OF_ORGANIZATIONS, organizationService.getListOfOrganizationDto());
        setPageAttribute(pageNum, sortField, sortDir, model);
        return INVOICE_UPDATE_HTML;
    }

    @PostMapping("/invoice-update")
    public String editInvoice(@ModelAttribute(INVOICE) @Valid InvoiceDto invoiceDto,
                              BindingResult bindingResult,
                              @RequestParam(SUPPLIER_DOT_ID) Integer idSupplier,
                              @RequestParam(RECEIVER_DOT_ID) Integer idReceiver,
                              int pageNum,
                              String sortField,
                              String sortDir,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ID, invoiceDto.getId());
            model.addAttribute(INVOICE, invoiceDto);
            model.addAttribute(LIST_OF_ORGANIZATIONS, organizationService.getListOfOrganizationDto());
            setPageAttribute(pageNum, sortField, sortDir, model);
            return INVOICE_UPDATE_HTML;
        }
        try {
            invoiceService.saveInvoice(invoiceDto, idSupplier, idReceiver);
        } catch (Exception ex) {
            model.addAttribute(ID, invoiceDto.getId());
            model.addAttribute(INVOICE, invoiceDto);
            model.addAttribute(LIST_OF_ORGANIZATIONS, organizationService.getListOfOrganizationDto());
            model.addAttribute(ERROR_MESSAGE, "Такая накладная уже создана.");
            setPageAttribute(pageNum, sortField, sortDir, model);
            return INVOICE_UPDATE_HTML;
        }
        return viewPage(model, pageNum, sortField, sortDir);
    }

    @GetMapping("/invoice-info/{id}/{pageNum}/{sortField}/{sortDir}")
    public String viewInvoiceInfo(Model model,
                                  @PathVariable(ID) Integer idInvoice,
                                  @PathVariable(PAGE_NUM) int pageNum,
                                  @PathVariable(SORT_FIELD) String sortField,
                                  @PathVariable(SORT_DIR) String sortDir) {
        model.addAttribute(INVOICE, invoiceService.getInvoice(idInvoice));
        setPageAttribute(pageNum, sortField, sortDir, model);
        return INVOICE_INFO_HTML;
    }

    @PostMapping("/invoice-chooseEq")
    private String chooseEquipmentForInvoice(@RequestParam(value = BOX_NAME_OF_EQUIPMENT, required = false) String[] equipments,
                                             @RequestParam(value = INVOICE_ID) Integer idInvoice,
                                             Model model) {
        if (equipments != null) {
            invoiceService.getEquipmentForInvoice(equipments, idInvoice);
        }
        return viewPage(model, FIRST_PAGE, ID, DESC_SORT);
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

package it.academy.accountingsb.managment.implementation;

import it.academy.accountingsb.dao.EquipmentRepository;
import it.academy.accountingsb.dao.InvoiceRepository;
import it.academy.accountingsb.dao.OrganizationRepository;
import it.academy.accountingsb.dto.EquipmentDto;
import it.academy.accountingsb.dto.InvoiceDto;
import it.academy.accountingsb.entity.Equipment;
import it.academy.accountingsb.entity.Invoice;
import it.academy.accountingsb.managment.interfaces.InvoiceService;
import it.academy.accountingsb.mappers.EquipmentMapper;
import it.academy.accountingsb.mappers.InvoiceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final OrganizationRepository organizationRepository;
    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceMapper invoiceMapper,
                              OrganizationRepository organizationRepository,
                              EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.organizationRepository = organizationRepository;
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    @Override
    @Transactional
    public InvoiceDto saveInvoice(InvoiceDto invoiceDto,
                                  Integer idSupplier,
                                  Integer idReceiver) {
        Invoice invoice = invoiceMapper.toInvoice(invoiceDto);
        invoice.setSupplier(organizationRepository.getReferenceById(idSupplier));
        invoice.setReceiver(organizationRepository.getReferenceById(idReceiver));
        invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceDto(invoice);
    }

    @Override
    public void delInvoice(Integer idInvoice) {
        invoiceRepository.deleteById(idInvoice);
    }

    @Override
    public InvoiceDto getInvoice(Integer idInvoice) {
        return invoiceMapper.toInvoiceDto(invoiceRepository.findById(idInvoice)
                .orElse(null));
    }

    @Override
    public List<InvoiceDto> getListOfInvoiceDto() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toInvoiceDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<InvoiceDto> invoiceByPage(int pageSize,
                                          int pageNum,
                                          String sortField,
                                          String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return invoiceRepository.findAll(pageable).map(invoiceMapper::toInvoiceDto);
    }

    @Override
    @Transactional
    public void getEquipmentForInvoice(String[] equipments,
                                       Integer idInvoice) {
        Invoice invoice = invoiceRepository.getReferenceById(idInvoice);
        for (String eq : equipments) {
            Equipment equipment = equipmentRepository.getReferenceById(Integer.parseInt(eq));
            List<Invoice> invoices = equipment.getInvoices();
            invoices.add(invoice);
            equipment.setInvoices(invoices);
            equipmentRepository.save(equipment);
        }
    }

    @Override
    public List<EquipmentDto> getEquipment(Integer idInvoice) {
        return invoiceRepository.showEquipmentsForInvoice(idInvoice).stream()
                .map(equipmentMapper::toEquipmentDto)
                .collect(Collectors.toList());
    }
}

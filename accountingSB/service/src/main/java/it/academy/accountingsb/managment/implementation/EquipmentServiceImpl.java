package it.academy.accountingsb.managment.implementation;

import it.academy.accountingsb.dao.EquipmentDetailRepository;
import it.academy.accountingsb.dao.EquipmentRepository;
import it.academy.accountingsb.dao.InvoiceRepository;
import it.academy.accountingsb.dto.EquipmentDto;
import it.academy.accountingsb.entity.Equipment;
import it.academy.accountingsb.entity.Invoice;
import it.academy.accountingsb.managment.interfaces.EquipmentService;
import it.academy.accountingsb.mappers.EquipmentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final InvoiceRepository invoiceRepository;
    private final EquipmentDetailRepository equipmentDetailRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository,
                                EquipmentMapper equipmentMapper,
                                InvoiceRepository invoiceRepository,
                                EquipmentDetailRepository equipmentDetailRepository) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
        this.invoiceRepository = invoiceRepository;
        this.equipmentDetailRepository = equipmentDetailRepository;
    }

    @Override
    @Transactional
    public void saveEquipment(EquipmentDto equipmentDto,
                              Integer idEquipmentDetail,
                              Integer idInvoiceNew,
                              Integer idInvoiceCurrent) {
        Equipment equipment = equipmentMapper.toEquipment(equipmentDto);
        List<Invoice> invoices = new ArrayList<>();
        if (equipment.getInvoices() != null) {
            invoices = equipment.getInvoices();
        }
        if (idInvoiceCurrent != null) {
            invoices.remove(invoiceRepository.getReferenceById(idInvoiceCurrent));
        }
        invoices.add(invoiceRepository.findById(idInvoiceNew).orElse(null));
        equipment.setEquipmentDetail(equipmentDetailRepository.getReferenceById(idEquipmentDetail));
        equipment.setInvoices(invoices);
        equipmentRepository.save(equipment);
    }

    @Override
    public void delEquipment(Integer idEquipment) {
        equipmentRepository.deleteById(idEquipment);
    }

    @Override
    public EquipmentDto getEquipment(Integer idEquipment) {
        return equipmentMapper.toEquipmentDto(equipmentRepository.findById(idEquipment)
                .orElse(null));
    }

    @Override
    public List<EquipmentDto> getListOfEquipment() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toEquipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<EquipmentDto> equipmentByPage(int pageSize,
                                              int pageNum,
                                              String sortField,
                                              String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return equipmentRepository.findAll(pageable).map(equipmentMapper::toEquipmentDto);
    }
}

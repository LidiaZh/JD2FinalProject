package it.academy.accountingsb.managment.implementation;


import it.academy.accountingsb.dao.EquipmentDetailRepository;
import it.academy.accountingsb.dto.EquipmentDetailDto;
import it.academy.accountingsb.managment.interfaces.EquipmentDetailService;
import it.academy.accountingsb.mappers.EquipmentDetailMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentDetailServiceImpl implements EquipmentDetailService {

    private final EquipmentDetailRepository equipmentDetailRepository;
    private final EquipmentDetailMapper equipmentDetailMapper;

    public EquipmentDetailServiceImpl(EquipmentDetailRepository equipmentDetailRepository,
                                      EquipmentDetailMapper equipmentDetailMapper) {
        this.equipmentDetailRepository = equipmentDetailRepository;
        this.equipmentDetailMapper = equipmentDetailMapper;
    }

    @Override
    public void saveEquipmentDetail(EquipmentDetailDto equipmentDetailDto) {
        equipmentDetailRepository.save(equipmentDetailMapper.toEquipmentDetail(equipmentDetailDto));
    }

    @Override
    public void delEquipmentDetail(Integer idEquipmentDetail) {
        equipmentDetailRepository.deleteById(idEquipmentDetail);
    }

    @Override
    public EquipmentDetailDto getEquipmentDetail(Integer idEquipmentDetail) {
        return equipmentDetailMapper.toEquipmentDetailDto(equipmentDetailRepository.findById(idEquipmentDetail)
                .orElse(null));
    }

    @Override
    public List<EquipmentDetailDto> getListOfEquipmentDetail() {
        return equipmentDetailRepository.findAll().stream()
                .map(equipmentDetailMapper::toEquipmentDetailDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<EquipmentDetailDto> equipmentDetailByPage(int pageSize,
                                                          int pageNum,
                                                          String sortField,
                                                          String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return equipmentDetailRepository.findAll(pageable)
                .map(equipmentDetailMapper::toEquipmentDetailDto);
    }
}

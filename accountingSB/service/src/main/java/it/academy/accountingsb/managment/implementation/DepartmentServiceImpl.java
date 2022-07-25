package it.academy.accountingsb.managment.implementation;


import it.academy.accountingsb.dao.DepartmentRepository;
import it.academy.accountingsb.dto.DepartmentDto;
import it.academy.accountingsb.managment.interfaces.DepartmentService;
import it.academy.accountingsb.mappers.DepartmentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public void saveDepartment(DepartmentDto departmentDto) {
        departmentRepository.save(departmentMapper.toDepartment(departmentDto));
    }

    @Override
    public void delDepartment(Integer idDepartment) {
        departmentRepository.deleteById(idDepartment);
    }

    @Override
    public DepartmentDto getDepartment(Integer idDepartment) {
        return departmentMapper.toDepartmentDto(departmentRepository.findById(idDepartment)
                .orElse(null));
    }

    @Override
    public List<DepartmentDto> getListOfDepartmentDto() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDepartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<DepartmentDto> departmentByPage(int pageSize,
                                                int pageNum,
                                                String sortField,
                                                String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return departmentRepository.findAll(pageable).map(departmentMapper::toDepartmentDto);
    }
}

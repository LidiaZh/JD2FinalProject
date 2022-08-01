package it.academy.accountingsb.managment.implementation;

import it.academy.accountingsb.dao.BranchRepository;
import it.academy.accountingsb.dao.DepartmentRepository;
import it.academy.accountingsb.dao.ResponsiblePersonRepository;
import it.academy.accountingsb.dto.BranchDto;
import it.academy.accountingsb.dto.DepartmentDto;
import it.academy.accountingsb.dto.ResponsiblePersonDto;
import it.academy.accountingsb.entity.ResponsiblePerson;
import it.academy.accountingsb.managment.interfaces.ResponsiblePersonService;
import it.academy.accountingsb.mappers.BranchMapper;
import it.academy.accountingsb.mappers.DepartmentMapper;
import it.academy.accountingsb.mappers.ResponsiblePersonMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponsiblePersonServiceImpl implements ResponsiblePersonService {

    private final ResponsiblePersonRepository resPersonRepository;
    private final ResponsiblePersonMapper resPersonMapper;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;

    public ResponsiblePersonServiceImpl(ResponsiblePersonRepository resPersonRepository,
                                        ResponsiblePersonMapper resPersonMapper,
                                        BranchRepository branchRepository,
                                        DepartmentRepository departmentRepository) {
        this.resPersonRepository = resPersonRepository;
        this.resPersonMapper = resPersonMapper;
        this.branchRepository = branchRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public void saveResponsiblePerson(ResponsiblePersonDto responsiblePersonDto,
                                      Integer idBranch,
                                      Integer idDepartment) {
        ResponsiblePerson responsiblePerson = resPersonMapper.toResPerson(responsiblePersonDto);
        responsiblePerson.setBranch(branchRepository.getReferenceById(idBranch));
        responsiblePerson.setDepartment(departmentRepository.getReferenceById(idDepartment));
        resPersonRepository.save(responsiblePerson);
    }

    @Override
    public void delResponsiblePerson(Integer idResponsiblePerson) {
        resPersonRepository.deleteById(idResponsiblePerson);
    }

    @Override
    public ResponsiblePersonDto getResponsiblePerson(Integer idResponsiblePerson) {
        return resPersonMapper.toResPersonDto(resPersonRepository.findById(idResponsiblePerson)
                .orElse(null));
    }

    @Override
    public List<ResponsiblePersonDto> getListOfResponsiblePerson() {
        return resPersonRepository.findAll().stream()
                .map(resPersonMapper::toResPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<ResponsiblePersonDto> responsiblePersonByPage(int pageSize,
                                                              int pageNum,
                                                              String sortField,
                                                              String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return resPersonRepository.findAll(pageable).map(resPersonMapper::toResPersonDto);
    }
}

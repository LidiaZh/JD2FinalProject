package it.academy.accountingsb.managment.implementation;

import it.academy.accountingsb.dao.BranchRepository;
import it.academy.accountingsb.dao.DepartmentRepository;
import it.academy.accountingsb.dto.BranchDto;
import it.academy.accountingsb.dto.DepartmentDto;
import it.academy.accountingsb.entity.Branch;
import it.academy.accountingsb.entity.Department;
import it.academy.accountingsb.managment.interfaces.BranchService;
import it.academy.accountingsb.mappers.BranchMapper;
import it.academy.accountingsb.mappers.DepartmentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;


    public BranchServiceImpl(BranchRepository branchRepository,
                             BranchMapper branchMapper,
                             DepartmentRepository departmentRepository,
                             DepartmentMapper departmentMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public void saveBranch(BranchDto branchDto) {
        branchRepository.save(branchMapper.toBranch(branchDto));
    }

    @Override
    public void delBranch(Integer idBranch) {
        branchRepository.deleteById(idBranch);
    }

    @Override
    public BranchDto getBranch(Integer idBranch) {
        return branchMapper.toBranchDto(branchRepository.findById(idBranch)
                .orElse(null));
    }

    @Override
    public List<BranchDto> getListOfBranchDto() {
        return branchRepository.findAll().stream()
                .map(branchMapper::toBranchDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDto> getDepartmentInBranch(Integer idBranch) {
        return branchRepository.showDepartmentInBranch(idBranch).stream()
                .map(departmentMapper::toDepartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addDepartmentInBranch(Integer idBranch,
                                      String[] departments) {
        Branch branch = branchRepository.getReferenceById(idBranch);
        Set<Department> departmentSet = new HashSet<>();
        if (branch.getDepartment() != null) {
            departmentSet = branch.getDepartment();
        }
        for (String dep : departments) {
            Department department = departmentRepository.getReferenceById(Integer.parseInt(dep));
            departmentSet.add(department);
        }
        branchRepository.save(branch);
    }

    @Override
    @Transactional
    public void delDepartmentFromBranch(Integer idBranch,
                                        Integer idDepartment) {
        Branch branch = branchRepository.getReferenceById(idBranch);
        Department department = departmentRepository.getReferenceById(idDepartment);
        Set<Department> departments;
        if (branch.getDepartment() != null) {
            departments = branch.getDepartment();
            departments.remove(department);
            branch.setDepartment(departments);
            branchRepository.save(branch);
        }
    }

    @Override
    @Transactional
    public Page<BranchDto> branchByPage(int pageSize,
                                        int pageNum,
                                        String sortField,
                                        String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return branchRepository.findAll(pageable).map(branchMapper::toBranchDto);
    }

}

package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.BranchDto;
import it.academy.accountingsb.dto.DepartmentDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BranchService {
    void saveBranch(BranchDto branchDto);

    void delBranch(Integer idBranch);

    BranchDto getBranch(Integer id);

    List<DepartmentDto> getDepartmentInBranch(Integer idBranch);

    void addDepartmentInBranch(Integer idBranch,
                               String[] departments);

    void delDepartmentFromBranch(Integer idBranch,
                                 Integer idDepartment);

    List<BranchDto> getListOfBranchDto();

    Page<BranchDto> branchByPage(int pageSize,
                                 int pageNum,
                                 String sortField,
                                 String sortDir);

}

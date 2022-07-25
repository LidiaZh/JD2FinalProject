package it.academy.accountingsb.managment.interfaces;

import it.academy.accountingsb.dto.DepartmentDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {

    void saveDepartment(DepartmentDto departmentDto);

    void delDepartment(Integer idDepartment);

    DepartmentDto getDepartment(Integer idDepartment);

    List<DepartmentDto> getListOfDepartmentDto();

    Page<DepartmentDto> departmentByPage(int pageSize,
                                         int pageNum,
                                         String sortField,
                                         String sortDir);
}

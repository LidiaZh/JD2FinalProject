package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.DepartmentDto;
import it.academy.accountingsb.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDto toDepartmentDto(Department department);

    Department toDepartment(DepartmentDto departmentDto);
}

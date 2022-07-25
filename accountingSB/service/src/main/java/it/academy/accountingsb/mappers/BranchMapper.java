package it.academy.accountingsb.mappers;

import it.academy.accountingsb.dto.BranchDto;
import it.academy.accountingsb.entity.Branch;
import org.mapstruct.Mapper;

import javax.xml.validation.Validator;

@Mapper(componentModel = "spring",
        uses = {DepartmentMapper.class, Validator.class})
public interface BranchMapper {
    BranchDto toBranchDto(Branch branch);

    Branch toBranch(BranchDto branchDto);

}

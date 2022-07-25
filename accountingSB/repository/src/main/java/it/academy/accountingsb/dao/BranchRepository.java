package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.Branch;
import it.academy.accountingsb.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer>,
        JpaSpecificationExecutor<Branch> {

    @Query("SELECT b.department FROM Branch b where b.id = :idBranch")
    List<Department> showDepartmentInBranch(@Param("idBranch") Integer idBranch);


}

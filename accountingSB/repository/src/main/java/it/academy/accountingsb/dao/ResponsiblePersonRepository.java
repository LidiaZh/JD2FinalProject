package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.ResponsiblePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResponsiblePersonRepository extends JpaRepository<ResponsiblePerson, Integer>,
        JpaSpecificationExecutor {
}

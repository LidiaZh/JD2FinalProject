package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrganizationRepository extends JpaRepository<Organization, Integer>,
        JpaSpecificationExecutor {
}

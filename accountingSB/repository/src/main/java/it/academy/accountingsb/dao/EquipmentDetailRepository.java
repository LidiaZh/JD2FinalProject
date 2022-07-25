package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.EquipmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentDetailRepository extends JpaRepository<EquipmentDetail, Integer>,
        JpaSpecificationExecutor {
}

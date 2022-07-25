package it.academy.accountingsb.dao;

import it.academy.accountingsb.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer>,
        JpaSpecificationExecutor {
}

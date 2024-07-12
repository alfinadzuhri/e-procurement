package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String>, JpaSpecificationExecutor<Warehouse> {
    List<Warehouse> findAllByVpId(String vpId);
}

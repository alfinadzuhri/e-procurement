package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, String>, JpaSpecificationExecutor<Vendor>  {
    List<Vendor> findAllByNameLike(String name);
    Optional<Vendor> findByName(String name);
}
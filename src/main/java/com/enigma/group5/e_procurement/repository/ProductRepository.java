package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    // query method
    List<Product> findAllByNameLike(String name);
    Optional<Product> findByName(String name);
}

package com.enigma.group5.e_procurement.repository;

import com.enigma.group5.e_procurement.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetail, String> {
}

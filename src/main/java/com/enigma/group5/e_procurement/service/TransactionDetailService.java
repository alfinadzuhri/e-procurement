package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> createBulk (List<TransactionDetail> transactionDetails);
}

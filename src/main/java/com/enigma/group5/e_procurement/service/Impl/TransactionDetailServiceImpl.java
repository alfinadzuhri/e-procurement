package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.entity.TransactionDetail;
import com.enigma.group5.e_procurement.repository.TransactionDetailsRepository;
import com.enigma.group5.e_procurement.service.TransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails) {
        return transactionDetailsRepository.saveAllAndFlush(transactionDetails);
    }
}

package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.request.SearchTransactionRequest;
import com.enigma.group5.e_procurement.dto.request.TransactionRequest;
import com.enigma.group5.e_procurement.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);

    Page<TransactionResponse> getAll(SearchTransactionRequest searchTransactionRequest);
}

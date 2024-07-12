package com.enigma.group5.e_procurement.controller;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.dto.request.SearchTransactionRequest;
import com.enigma.group5.e_procurement.dto.request.TransactionRequest;
import com.enigma.group5.e_procurement.dto.response.*;
import com.enigma.group5.e_procurement.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<NewTransactionResponse<TransactionResponse>> createNewTransaction(@RequestBody TransactionRequest transactionRequest){

        TransactionResponse newTransaction = transactionService.create(transactionRequest);
        List<TransactionDetailResponse> newTransactionDetail = newTransaction.getTransactionDetailResponses();

        NewTransactionResponse<TransactionResponse> response = NewTransactionResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Transaction")
                .transactionId(newTransaction.getId())
                .transactionDate(newTransaction.getTransactionDate())
                .vendorName(newTransaction.getVendorName())
                .data(newTransactionDetail)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "4") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ){
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();

        Page<TransactionResponse> allTransactions = transactionService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(allTransactions.getTotalPages())
                .totalElements(allTransactions.getTotalElements())
                .page(allTransactions.getPageable().getPageNumber() + 1)
                .size(allTransactions.getPageable().getPageSize())
                .hasNext(allTransactions.hasNext())
                .hasPrevious(allTransactions.hasPrevious())
                .build();

        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success get all product")
                .data(allTransactions.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}

package com.enigma.group5.e_procurement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewTransactionResponse<T> {
    private Integer statusCode;
    private String message;
    private String transactionId;
    private String vendorName;
    private Date transactionDate;
    private List<TransactionDetailResponse> data; // nanti datanya bisa dinamis, array atau object
    private PagingResponse paging;
}

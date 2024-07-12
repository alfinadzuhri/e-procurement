package com.enigma.group5.e_procurement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private String vendorName;
    private Date transactionDate;
    private List<TransactionDetailResponse> transactionDetailResponses;
}

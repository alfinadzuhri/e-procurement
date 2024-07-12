package com.enigma.group5.e_procurement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    @NotBlank(message = "vendor id is required")
    private String vendorId;

    private List<TransactionDetailRequest> transactionDetail;
}

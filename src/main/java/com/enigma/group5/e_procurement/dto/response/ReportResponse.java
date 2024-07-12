package com.enigma.group5.e_procurement.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReportResponse {

    private String productId;
    private Date transactionDate;
    private String vendorName;
    private String productName;
    private String category;
    private Long price;
    private Integer quantity;
    private Long total;
}

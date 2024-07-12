package com.enigma.group5.e_procurement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchWarehouseResponse {
    private String vendorId;
    private String vendorName;
    private String vendorAddress;
    private String vendorPhone;
    private String productId;
    private String productName;
    private String category;
    private String vpId;
    private Long price;
    private Integer stock;
}

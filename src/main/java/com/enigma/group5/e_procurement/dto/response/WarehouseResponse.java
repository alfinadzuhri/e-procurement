package com.enigma.group5.e_procurement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseResponse {
    private String warehouseId;
    private String vendorName;
    private String productName;
    private String productCategory;
    private String productDescription;
    private Long price;
    private Integer stock;
    private String ImagePath;
}

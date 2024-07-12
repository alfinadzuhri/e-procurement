package com.enigma.group5.e_procurement.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchWarehouseRequest {
    private String vpId;
    private String transDetail;
    private Integer stock;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String name;
}

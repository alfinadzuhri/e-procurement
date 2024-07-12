package com.enigma.group5.e_procurement.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchVendorProductRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
}

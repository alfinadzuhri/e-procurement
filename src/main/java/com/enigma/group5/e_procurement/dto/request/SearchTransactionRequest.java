package com.enigma.group5.e_procurement.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTransactionRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String name;
}

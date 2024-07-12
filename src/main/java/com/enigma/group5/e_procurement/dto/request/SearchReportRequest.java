package com.enigma.group5.e_procurement.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SearchReportRequest {

    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String transDate;
    private Date startDate;
    private Date endDate;
}

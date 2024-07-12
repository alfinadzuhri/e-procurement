package com.enigma.group5.e_procurement.dto.response;

import lombok.*;
import org.springframework.core.io.Resource;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorProductResponse {
    private String vendorProductId;
    private String vendorName;
    private String vendorAddress;
    private String productName;
    private String productCategory;
    private Long price;
    private ImageResponse imageResponse;
    private String productDescription;
}

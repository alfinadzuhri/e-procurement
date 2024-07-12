package com.enigma.group5.e_procurement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    // url = "/api/v1/products/images/{id}
    private String url;
    private String name;
}

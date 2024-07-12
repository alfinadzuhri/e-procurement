package com.enigma.group5.e_procurement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtClaims {
    // ini adalah data yg kita dapatkan dari Payload: Data Jwtnya
    private String userAccountId;
    private List<String> roles;
}


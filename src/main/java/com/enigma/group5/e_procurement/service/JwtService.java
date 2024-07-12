package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.response.JwtClaims;
import com.enigma.group5.e_procurement.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);

    boolean verifyJwtToken(String token);

    JwtClaims getClaimsByToken(String token);
}


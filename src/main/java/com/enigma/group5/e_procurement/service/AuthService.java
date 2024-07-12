package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.request.AuthRequest;
import com.enigma.group5.e_procurement.dto.response.LoginResponse;
import com.enigma.group5.e_procurement.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}

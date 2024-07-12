package com.enigma.group5.e_procurement.controller;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.dto.request.AuthRequest;
import com.enigma.group5.e_procurement.dto.response.CommonResponse;
import com.enigma.group5.e_procurement.dto.response.LoginResponse;
import com.enigma.group5.e_procurement.dto.response.RegisterResponse;
import com.enigma.group5.e_procurement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.AUTH_API)

public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody AuthRequest request){
        RegisterResponse register = authService.register(request);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully save data")
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request){
        LoginResponse loginResponse = authService.login(request);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("login successfully")
                .data(loginResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}


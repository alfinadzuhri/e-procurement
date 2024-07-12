package com.enigma.group5.e_procurement.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewVendorRequest {
    @NotBlank(message = "Name is Required")
    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^08\\d{9,11}$", message = "Nomor telepon harus valid dan diawali dengan '08' diikuti oleh 9 hingga 11 angka.")
    @NotBlank(message = "Phone Number is Required")
    @Column(name = "mobile_phone_no")
    private String mobilePhoneNo;

    @NotBlank(message = "Address is Required")
    @Column(name = "address")
    private String address;

    @Email(message = "Email Format Not Valid")
    @Pattern(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$" , message = "Email tidak sesuai format")
    @Column(name = "email")
    private String email;
}

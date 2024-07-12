package com.enigma.group5.e_procurement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductRequest {
    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Category is Required")
    private String category;

    @NotBlank(message = "Description is Required")
    private String description;

    private MultipartFile image;
}

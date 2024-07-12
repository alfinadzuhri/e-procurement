package com.enigma.group5.e_procurement.controller;

import com.enigma.group5.e_procurement.service.ImageService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    private static final String IMAGE_DIRECTORY = "/home/enigma/Documents/Procurement_Images";

    @GetMapping("/api/v1/products/images")
    public ResponseEntity<Resource> getImage(@RequestParam String filename) throws IOException {
        File imgFile = new File(IMAGE_DIRECTORY, filename);

        if (!imgFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(imgFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imgFile.length());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/api/v1/products/images/{imageId}")
    public ResponseEntity<?> downloadImage(
            @PathVariable(name = "imageId")	String id
    ){
        Resource imageById = imageService.getById(id);

        //  "attachment; filename="filename.jpg""
        String headerValue = String.format("attachment; filename=%s",imageById.getFilename());

        // Content-Disposition: attachment; filename="filename.jpg"
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(imageById);

    }
}

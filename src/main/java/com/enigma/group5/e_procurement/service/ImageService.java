package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image create(MultipartFile multipartFile);

    Resource getById(String id);

    Image searchById(String id);

    void deleteById(String id);
}

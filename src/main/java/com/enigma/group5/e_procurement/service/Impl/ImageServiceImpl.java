package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.entity.Image;
import com.enigma.group5.e_procurement.repository.ImageRepository;
import com.enigma.group5.e_procurement.service.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final Path directoryPath;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, @Value("${procurement.multipart.path_location}") String directoryPath) {
        this.imageRepository = imageRepository;
        this.directoryPath = Paths.get(directoryPath);
    }

    @PostConstruct
    public void initDirectory() {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public Image create(MultipartFile multipartFile) {

        try {
            if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType())) {
                throw new ConstraintViolationException("invalid image format", null);
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

            Path filePath = directoryPath.resolve(uniqueFileName);
            Files.copy(multipartFile.getInputStream(), filePath);

            Image image = Image.builder()
                    .name(uniqueFileName)
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .path(filePath.toString())
                    .build();
            imageRepository.saveAndFlush(image);

            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Resource getById(String id) {
        try {
            // cari image berdasarkan id, untu dapet path nya
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

            // cari gambar berdasarkan pathnya
            Path imagePath = Paths.get(image.getPath());
            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");
            }

            // kirim object gambarnya
            return new UrlResource(imagePath.toUri());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Image searchById(String id) {

        Optional<Image> searchById = imageRepository.findById(id);

        return searchById.get();
    }

    @Override
    public void deleteById(String id) {
        try {
            // cari image berdasarkan id, untu dapet path nya
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

            // cari gambar berdasarkan pathnya
            Path imagePath = Paths.get(image.getPath());
            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");
            }

            // untuk delete file di directory
            Files.delete(imagePath);

            // untuk delete di database
            imageRepository.delete(image);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

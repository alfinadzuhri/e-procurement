package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.dto.request.NewProductRequest;
import com.enigma.group5.e_procurement.dto.request.SearchProductRequest;
import com.enigma.group5.e_procurement.dto.response.ImageResponse;
import com.enigma.group5.e_procurement.dto.response.ProductResponse;
import com.enigma.group5.e_procurement.entity.Image;
import com.enigma.group5.e_procurement.entity.Product;
import com.enigma.group5.e_procurement.repository.ProductRepository;
import com.enigma.group5.e_procurement.service.ImageService;
import com.enigma.group5.e_procurement.service.ProductService;
import com.enigma.group5.e_procurement.specification.ProductSpecification;
import com.enigma.group5.e_procurement.utils.ValidationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ValidationUtil validationUtil;

    private final ImageService imageService;

    private List<NewProductRequest> createNewProductRequest() {

        List<NewProductRequest> listProduct = new ArrayList<>();

        try (FileInputStream indomieStream = new FileInputStream("src/main/resources/custom-assets/images/indomie.png");
             FileInputStream qtelaStream = new FileInputStream("src/main/resources/custom-assets/images/qtela.png");
             FileInputStream keyboardStream = new FileInputStream("src/main/resources/custom-assets/images/keyboard.jpg");
             FileInputStream mouseStream = new FileInputStream("src/main/resources/custom-assets/images/Mouse.jpg");
             FileInputStream eyelashStream = new FileInputStream("src/main/resources/custom-assets/images/eyelash.jpeg");
             FileInputStream lipstickStream = new FileInputStream("src/main/resources/custom-assets/images/lipstick.jpg")) {

            MultipartFile indomieImage = new MockMultipartFile("file", "indomie.png", "image/png", indomieStream);
            MultipartFile qtelaImage = new MockMultipartFile("file", "qtela.png", "image/png", qtelaStream);
            MultipartFile keyboardImage = new MockMultipartFile("file", "keyboard.jpg", "image/jpeg", keyboardStream);
            MultipartFile mouseImage = new MockMultipartFile("file", "mouse.jpg", "image/jpeg", mouseStream);
            MultipartFile eyelashImage = new MockMultipartFile("file", "eyelash.jpeg", "image/jpeg", eyelashStream);
            MultipartFile lipstickImage = new MockMultipartFile("file", "lipstick.jpg", "image/jpeg", lipstickStream);

            listProduct.add(NewProductRequest.builder()
                    .name("Indomie")
                    .category("food")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam aspernatur quos repudiandae necessitatibus est veritatis, ratione sed doloribus quia minus maxime facere amet perspiciatis eos!")
                    .image(indomieImage)
                    .build());

            listProduct.add(NewProductRequest.builder()
                    .name("Q'tela")
                    .category("food")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam aspernatur quos repudiandae necessitatibus est veritatis, ratione sed doloribus quia minus maxime facere amet perspiciatis eos!")
                    .image(qtelaImage)
                    .build());

            listProduct.add(NewProductRequest.builder()
                    .name("Corsair K55 CORE RGB Membrane Wired Gaming Keyboard")
                    .category("electronics")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam aspernatur quos repudiandae necessitatibus est veritatis, ratione sed doloribus quia minus maxime facere amet perspiciatis eos!")
                    .image(keyboardImage)
                    .build());

            listProduct.add(NewProductRequest.builder()
                    .name("RisoPhy Wireless Gaming Mouse")
                    .category("electronics")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam aspernatur quos repudiandae necessitatibus est veritatis, ratione sed doloribus quia minus maxime facere amet perspiciatis eos!")
                    .image(mouseImage)
                    .build());

            listProduct.add(NewProductRequest.builder()
                    .name("2023 best eyelash extensions lash kit")
                    .category("beauty")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam aspernatur quos repudiandae necessitatibus est veritatis, ratione sed doloribus quia minus maxime facere amet perspiciatis eos!")
                    .image(eyelashImage)
                    .build());

            listProduct.add(NewProductRequest.builder()
                    .name("handaiyan holographic lipgloss 6pcs")
                    .category("beauty")
                    .description("Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam aspernatur quos repudiandae necessitatibus est veritatis, ratione sed doloribus quia minus maxime facere amet perspiciatis eos!")
                    .image(lipstickImage)
                    .build());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listProduct;
    }

    @PostConstruct
    public void initVendor() {
        List<Product> existingProduct = productRepository.findAll();
        if (!existingProduct.isEmpty()) {
            return;
        }
        List<NewProductRequest> newProducts = createNewProductRequest();
        List<Product> products = newProducts.stream().map(product -> {
           Image image = imageService.create(product.getImage());
            return Product.builder()
                    .name(product.getName())
                    .category(product.getCategory())
                    .description(product.getDescription())
                    .image(image)
                    .build();
        }).toList();

        productRepository.saveAllAndFlush(products);
    }

    @Override
    public ProductResponse create(NewProductRequest productRequest) {
        validationUtil.validate(productRequest);
        if (productRequest.getImage().isEmpty()){
            throw new ConstraintViolationException("image is required", null);
        }

        Image image = imageService.create(productRequest.getImage());

        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .category(productRequest.getCategory())
                .image(image)
                .build();
        productRepository.saveAndFlush(newProduct);
        return parseProductToProductResponse(newProduct);
    }

    @Override
    public Product getById(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        return optionalProduct.get();
    }

    @Override
    public Page<Product> getAll(SearchProductRequest productRequest) {
        if (productRequest.getPage() <= 0){
            productRequest.setPage(1);
        }
        String validSortBy;
        if ("name".equalsIgnoreCase(productRequest.getSortBy()) || "category".equalsIgnoreCase(productRequest.getSortBy())){
            validSortBy = productRequest.getSortBy();
        }else {
            validSortBy = "name";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(productRequest.getDirection()), /*productRequest.getSortBy()*/ validSortBy);

        Pageable pageable = PageRequest.of((productRequest.getPage() -1), productRequest.getSize(), sort); // rumus pagination

        Specification<Product> specification = ProductSpecification.getSpecification(productRequest);

        return productRepository.findAll(specification,pageable);
    }

    @Override
    public Product update(Product product) {
        getById(product.getId());
        return productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteById(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
    }

    private ProductResponse parseProductToProductResponse(Product product){

        String imageId;
        String name;
        if(product.getImage() == null){
            imageId = null;
            name = null;
        } else {
            imageId = product.getImage().getId();
            name = product.getImage().getName();
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .image(ImageResponse.builder()
                        .url(APIUrl.PRODUCT_IMAGE_DOWNLOAD_API + imageId)
                        .name(name)
                        .build())
                .build();
    }
}

package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.dto.request.SearchVendorProductRequest;
import com.enigma.group5.e_procurement.dto.response.VendorProductResponse;
import com.enigma.group5.e_procurement.dto.response.WarehouseResponse;
import com.enigma.group5.e_procurement.entity.Image;
import com.enigma.group5.e_procurement.entity.Product;
import com.enigma.group5.e_procurement.entity.Vendor;
import com.enigma.group5.e_procurement.entity.VendorProduct;
import com.enigma.group5.e_procurement.repository.VendorProductRepository;
import com.enigma.group5.e_procurement.service.ImageService;
import com.enigma.group5.e_procurement.service.ProductService;
import com.enigma.group5.e_procurement.service.VendorProductService;
import com.enigma.group5.e_procurement.service.VendorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VendorProductServiceImpl implements VendorProductService {

    private final VendorProductRepository vendorProductRepository;
    private final VendorService vendorService;
    private final ProductService productService;
    private final ImageService imageService;

    private List<VendorProduct> vendorProducts() {
        List<VendorProduct> vendorProductList = new ArrayList<>();

        vendorProductList.add(VendorProduct.builder()
                .vendor(vendorService.findByName("salim"))
                .product(productService.findByName("Indomie"))
                .price(4000l)
                .build());

        vendorProductList.add(VendorProduct.builder()
                .vendor(vendorService.findByName("salim"))
                .product(productService.findByName("Q'tela"))
                .price(10000l)
                .build());

        vendorProductList.add(VendorProduct.builder()
                .vendor(vendorService.findByName("amazon"))
                .product(productService.findByName("Corsair K55 CORE RGB Membrane Wired Gaming Keyboard"))
                .price(100000l)
                .build());

        vendorProductList.add(VendorProduct.builder()
                .vendor(vendorService.findByName("amazon"))
                .product(productService.findByName("RisoPhy Wireless Gaming Mouse"))
                .price(150000l)
                .build());

        vendorProductList.add(VendorProduct.builder()
                .vendor(vendorService.findByName("alibaba"))
                .product(productService.findByName("2023 best eyelash extensions lash kit"))
                .price(5000l)
                .build());

        vendorProductList.add(VendorProduct.builder()
                .vendor(vendorService.findByName("alibaba"))
                .product(productService.findByName("handaiyan holographic lipgloss 6pcs"))
                .price(70000l)
                .build());
        return vendorProductList;
    }

    @PostConstruct
    public void initVendor() {
        List<VendorProduct> existingVendorsProducts = vendorProductRepository.findAll();
        if (!existingVendorsProducts.isEmpty()) {
            return;
        }

        List<VendorProduct> newVendorProducts = vendorProducts();

        vendorProductRepository.saveAllAndFlush(newVendorProducts);
    }

    @Override
    public Page<VendorProductResponse> getAll(SearchVendorProductRequest vendorProductRequest) {
        if (vendorProductRequest.getPage() <= 0){
            vendorProductRequest.setPage(1);
        }

        String validSortBy;
        if ("vpId".equalsIgnoreCase(vendorProductRequest.getSortBy()) || "transDetail".equalsIgnoreCase(vendorProductRequest.getSortBy())
                || "stock".equals(vendorProductRequest.getSortBy())) {
            validSortBy = vendorProductRequest.getSortBy();
        }
        else {
            validSortBy = "vendorId";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(vendorProductRequest.getDirection()), validSortBy);

        Pageable pageable = PageRequest.of(vendorProductRequest.getPage() - 1, vendorProductRequest.getSize(), sort);

        List<VendorProductResponse> responses = vendorProductRepository.findAll().stream().map(vendorProduct -> parseToVendorProductResponse(vendorProduct)).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responses.size());

        List<VendorProductResponse> pageContent = responses.subList(start, end);

        return new PageImpl<>(pageContent, pageable, responses.size());
    }

    @Override
    public VendorProduct getById(String id) {
        Optional<VendorProduct> optionalVendorProduct = vendorProductRepository.findById(id);

        return optionalVendorProduct.orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    private VendorProductResponse parseToVendorProductResponse(VendorProduct vendorProduct){
        Vendor vendor = vendorService.getById(vendorProduct.getVendor().getId());
        Product product = productService.getById(vendorProduct.getProduct().getId());
        Image image = imageService.searchById(product.getImage().getId());

        return VendorProductResponse.builder()
                .vendorProductId(vendorProduct.getId())
                .vendorName(vendor.getName())
                .vendorAddress(vendor.getAddress())
                .productName(product.getName())
                .productCategory(product.getCategory())
                .price(vendorProduct.getPrice())
                .imagePath(image.getPath())
                .productDescription(product.getDescription())
                .build();
    }
}

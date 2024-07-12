package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.entity.VendorProduct;
import com.enigma.group5.e_procurement.repository.VendorProductRepository;
import com.enigma.group5.e_procurement.service.ProductService;
import com.enigma.group5.e_procurement.service.VendorProductService;
import com.enigma.group5.e_procurement.service.VendorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
    public VendorProduct getById(String id) {
        Optional<VendorProduct> optionalVendorProduct = vendorProductRepository.findById(id);

        return optionalVendorProduct.orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}

package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.entity.VendorProduct;
import com.enigma.group5.e_procurement.repository.VendorProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorProductService implements com.enigma.group5.e_procurement.service.VendorProductService {
    private final VendorProductRepository vendorProductRepository;

    @Override
    public VendorProduct getById(String id) {
        Optional<VendorProduct> optionalVendorProduct = vendorProductRepository.findById(id);

        if (optionalVendorProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot Find Warehouse Vendor");
        }

        return optionalVendorProduct.get();
    }
}

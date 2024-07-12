package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.request.SearchVendorProductRequest;
import com.enigma.group5.e_procurement.dto.request.SearchVendorRequest;
import com.enigma.group5.e_procurement.dto.response.VendorProductResponse;
import com.enigma.group5.e_procurement.entity.Vendor;
import com.enigma.group5.e_procurement.entity.VendorProduct;
import org.springframework.data.domain.Page;

public interface VendorProductService {
    Page<VendorProductResponse> getAll(SearchVendorProductRequest vendorProductRequest);
    VendorProduct getById(String id);
}

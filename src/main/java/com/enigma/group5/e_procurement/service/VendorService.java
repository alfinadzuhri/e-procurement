package com.enigma.group5.e_procurement.service;

import com.enigma.group5.e_procurement.dto.request.NewVendorRequest;
import com.enigma.group5.e_procurement.dto.request.SearchVendorRequest;
import com.enigma.group5.e_procurement.entity.Vendor;
import org.springframework.data.domain.Page;

public interface VendorService {
    Vendor create(NewVendorRequest vendorRequest);
    Vendor getById(String id);
    Page<Vendor> getAll(SearchVendorRequest vendorRequest);
    Vendor update(Vendor vendor);
    void deleteById(String id);
    Vendor findByName(String name);
}

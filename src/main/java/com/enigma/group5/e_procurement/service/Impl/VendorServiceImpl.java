package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.dto.request.NewVendorRequest;
import com.enigma.group5.e_procurement.dto.request.SearchVendorRequest;
import com.enigma.group5.e_procurement.entity.Vendor;
import com.enigma.group5.e_procurement.repository.VendorRepository;
import com.enigma.group5.e_procurement.service.VendorService;
import com.enigma.group5.e_procurement.specification.VendorSpecification;
import com.enigma.group5.e_procurement.utils.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final ValidationUtil validationUtil;

    private List<NewVendorRequest> createNewVendorRequests() {
        List<NewVendorRequest> listVendors = new ArrayList<>();

        listVendors.add(NewVendorRequest.builder()
                .name("alibaba")
                .email("alibaba@alibaba.com")
                .address("Hangzhou 311121 Zhejiang Province, China")
                .mobilePhoneNo("08571850220")
                .build());

        listVendors.add(NewVendorRequest.builder()
                .name("amazon")
                .email("amazon@amazon.com")
                .address("Terry Avenue North, Seattle, Washington, 98109")
                .mobilePhoneNo("08266100093")
                .build());

        listVendors.add(NewVendorRequest.builder()
                .name("salim")
                .email("salim@salim.com")
                .address("TTbk Sudirman Plaza Indofood Tower Lt. 11. Jl. Jend Sudirman Kav. 76-78, Jakarta 12910")
                .mobilePhoneNo("08579588220")
                .build());

        return listVendors;
    }

    @PostConstruct
    public void initVendor() {
        List<Vendor> existingVendors = vendorRepository.findAll();
        if (!existingVendors.isEmpty()) {
            return;
        }

        List<NewVendorRequest> newVendors = createNewVendorRequests();
        List<Vendor> vendors = newVendors.stream().map(vendor -> Vendor.builder()
                .name(vendor.getName())
                .email(vendor.getEmail())
                .address(vendor.getAddress())
                .mobilePhoneNo(vendor.getMobilePhoneNo())
                .build()).toList();

        vendorRepository.saveAllAndFlush(vendors);
    }

    @Override
    public Vendor create(NewVendorRequest vendorRequest) {
        validationUtil.validate(vendorRequest); // wajib dilakukan sebelum membuat logic-logic
        Vendor newVendor = Vendor.builder()
                .name(vendorRequest.getName())
                .mobilePhoneNo(vendorRequest.getMobilePhoneNo())
                .address(vendorRequest.getAddress())
                .email(vendorRequest.getEmail())
                .build();
        return vendorRepository.saveAndFlush(newVendor);
    }

    @Override
    public Vendor getById(String id) {
        Optional<Vendor> optionalVendor = vendorRepository.findById(id);
        if (optionalVendor.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vendor not found");
        }
        return optionalVendor.get();
    }

    @Override
    public Page<Vendor> getAll(SearchVendorRequest vendorRequest) {
        if (vendorRequest.getPage() <= 0){
            vendorRequest.setPage(1);
        }
        String validSortBy;
        if ("name".equalsIgnoreCase(vendorRequest.getSortBy())){
            validSortBy = vendorRequest.getSortBy();
        }else {
            validSortBy = "name";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(vendorRequest.getDirection()), /*productRequest.getSortBy()*/ validSortBy);

        Pageable pageable = PageRequest.of((vendorRequest.getPage() -1), vendorRequest.getSize(), sort); // rumus pagination

        Specification<Vendor> specification = VendorSpecification.getSpecification(vendorRequest);

        return vendorRepository.findAll(specification,pageable);
    }

    @Override
    public Vendor update(Vendor vendor) {
        getById(vendor.getId());
        return vendorRepository.saveAndFlush(vendor);
    }

    @Override
    public void deleteById(String id) {
        Vendor currentVendor = getById(id);
        vendorRepository.delete(currentVendor);
    }

    @Override
    public Vendor findByName(String name) {
        return vendorRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Vendor not found with name: " + name));
    }


}

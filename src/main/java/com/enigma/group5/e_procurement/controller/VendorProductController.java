package com.enigma.group5.e_procurement.controller;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.constant.ResponseMessage;
import com.enigma.group5.e_procurement.dto.request.SearchVendorProductRequest;
import com.enigma.group5.e_procurement.dto.request.SearchVendorRequest;
import com.enigma.group5.e_procurement.dto.response.CommonResponse;
import com.enigma.group5.e_procurement.dto.response.PagingResponse;
import com.enigma.group5.e_procurement.dto.response.VendorProductResponse;
import com.enigma.group5.e_procurement.entity.Vendor;
import com.enigma.group5.e_procurement.service.VendorProductService;
import com.enigma.group5.e_procurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.VENDOR_PRODUCT_API)
public class VendorProductController {

    private final VendorProductService vendorProductService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<VendorProductResponse>>> getAllVendor(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {
        SearchVendorProductRequest request = SearchVendorProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .build();

        Page<VendorProductResponse> vendorAll = vendorProductService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(vendorAll.getTotalPages())
                .totalElements(vendorAll.getTotalElements())
                .page(vendorAll.getPageable().getPageNumber())
                .size(vendorAll.getPageable().getPageSize())
                .hasNext(vendorAll.hasNext())
                .hasPrevious(vendorAll.hasPrevious())
                .build();

        CommonResponse<List<VendorProductResponse>> response = CommonResponse.<List<VendorProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(vendorAll.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}

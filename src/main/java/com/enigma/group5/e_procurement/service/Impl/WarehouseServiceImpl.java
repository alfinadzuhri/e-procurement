package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.constant.APIUrl;
import com.enigma.group5.e_procurement.dto.request.SearchWarehouseRequest;
import com.enigma.group5.e_procurement.dto.response.ImageResponse;
import com.enigma.group5.e_procurement.dto.response.WarehouseResponse;
import com.enigma.group5.e_procurement.entity.*;
import com.enigma.group5.e_procurement.repository.WarehouseRepository;
import com.enigma.group5.e_procurement.service.*;
import com.enigma.group5.e_procurement.service.VendorProductService;
import com.enigma.group5.e_procurement.specification.WarehouseSpecification;
import com.enigma.group5.e_procurement.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final VendorService vendorService;
    private final ProductService productService;
    private final VendorProductService vendorProductService;
    private final ImageService imageService;

    private final ValidationUtil validationUtil;
    @Override
    public Warehouse findByVendorProductId(String vpId) {
       List<Warehouse> optionalWarehouse = warehouseRepository.findAllByVpId(vpId);
        if (optionalWarehouse.size() == 0){
            return null;
        } else {
            return optionalWarehouse.get(0);
        }
    }

    @Transactional
    @Override
    public void create(Transaction transaction) {

        validationUtil.validate(transaction);
        List<TransactionDetail> transactionDetails = transaction.getTransactionDetails();

        List<Warehouse> warehouses = new ArrayList<>();

        transactionDetails.stream().map(
                transactionDetailList -> {

                    Warehouse findByVpId = findByVendorProductId(transactionDetailList.getVendorProduct().getId());

                    //if vendor product in warehouse doesn't exist
                    if (findByVpId == null) {
                        Warehouse warehouse = Warehouse.builder()
                                .transaction(transaction)
                                .vpId(transactionDetailList.getVendorProduct().getId())
                                .price(transactionDetailList.getPrice())
                                .stock(transactionDetailList.getQuantity())
                                .build();

                        warehouses.add(warehouse);

                    //if vendor product in warehouse exist
                    } else if (findByVpId != null) {
                        Warehouse warehouse = new Warehouse();

                        if (findByVpId.getPrice() != transactionDetailList.getPrice()){
                            warehouse = Warehouse.builder()
                                    .transaction(transaction)
                                    .vpId(transactionDetailList.getVendorProduct().getId())
                                    .price(transactionDetailList.getPrice())
                                    .stock(transactionDetailList.getQuantity())
                                    .build();

                            warehouses.add(warehouse);
                        } else {
                            warehouse = findByVpId;
                            warehouse.setStock(findByVpId.getStock() + transactionDetailList.getQuantity());
                            warehouses.add(warehouse);
                        }

                    }
                    return warehouses;
                }).toList();

        warehouseRepository.saveAllAndFlush(warehouses);
    }

    public Warehouse findByIdOrThrowNotFound(String id) {
        return warehouseRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public Warehouse getById(String id) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(id);
        if (optionalWarehouse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!");
        }
        return optionalWarehouse.get();
    }

    @Override
    public Page<WarehouseResponse> getAll(SearchWarehouseRequest warehouseRequest) {
        if (warehouseRequest.getPage() <= 0){
            warehouseRequest.setPage(1);
        }

        String validSortBy;
        if ("vpId".equalsIgnoreCase(warehouseRequest.getSortBy()) || "transDetail".equalsIgnoreCase(warehouseRequest.getSortBy())
        || "stock".equals(warehouseRequest.getSortBy())) {
            validSortBy = warehouseRequest.getSortBy();
        }
        else {
            validSortBy = "vendorId";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(warehouseRequest.getDirection()), validSortBy);

        Pageable pageable = PageRequest.of(warehouseRequest.getPage() - 1, warehouseRequest.getSize(), sort);
        Specification<Warehouse> specification = WarehouseSpecification.getSpecification(warehouseRequest);

        List<WarehouseResponse> warehouses = warehouseRepository.findAll().stream().map(
                warehouse -> parseToWarehouseResponse(warehouse)
        ).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), warehouses.size());

        List<WarehouseResponse> pageContent = warehouses.subList(start, end);

        return new PageImpl<>(pageContent, pageable, warehouses.size());
    }

    @Override
    public Warehouse update(Warehouse warehouse) {
        getById(warehouse.getId());
        return warehouseRepository.saveAndFlush(warehouse);
    }

    @Override
    public void deleteById(String id) {
        Warehouse warehouse = findByIdOrThrowNotFound(id);
        warehouseRepository.delete(warehouse);
    }

    private WarehouseResponse parseToWarehouseResponse(Warehouse warehouse){

        VendorProduct vendorProduct = vendorProductService.getById(warehouse.getVpId());
        Vendor vendor = vendorService.getById(vendorProduct.getVendor().getId());
        Product product = productService.getById(vendorProduct.getProduct().getId());
        Image image = imageService.searchById(product.getImage().getId());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        ImageResponse imageResponse = ImageResponse.builder()
                .name(image.getName())
                .url(baseUrl + APIUrl.PRODUCT_IMAGE_API + image.getName())
                .build();

        return WarehouseResponse.builder()
                .warehouseId(warehouse.getId())
                .vendorName(vendor.getName())
                .productName(product.getName())
                .productCategory(product.getCategory())
                .productDescription(product.getDescription())
                .price(warehouse.getPrice())
                .stock(warehouse.getStock())
                .imageResponse(imageResponse)
                .build();
    }

}

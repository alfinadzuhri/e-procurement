package com.enigma.group5.e_procurement.service.Impl;

import com.enigma.group5.e_procurement.dto.request.SearchTransactionRequest;
import com.enigma.group5.e_procurement.dto.request.TransactionRequest;
import com.enigma.group5.e_procurement.dto.response.TransactionDetailResponse;
import com.enigma.group5.e_procurement.dto.response.TransactionResponse;
import com.enigma.group5.e_procurement.entity.*;
import com.enigma.group5.e_procurement.repository.TransactionRepository;
import com.enigma.group5.e_procurement.service.*;
import com.enigma.group5.e_procurement.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final VendorService vendorService;
    private final VendorProductService vendorProductService;
    private final TransactionDetailService transactionDetailService;
    private final WarehouseService warehouseService;
    private final ReportService reportService;
    private final ProductService productService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {

        //validation
        validationUtil.validate(request);

        Vendor vendor = vendorService.getById(request.getVendorId());

        //save to transaction
        Transaction transaction = Transaction.builder()
                .vendor(vendor)
                .transactionDate(new Date())
                .build();
        transactionRepository.saveAndFlush(transaction);

        //save to Transaction Detail
        List<TransactionDetail> transactionDetails = request.getTransactionDetail().stream().map(details -> {

            //validation
            validationUtil.validate(details);

            //Check if vendor product is exist
            VendorProduct vendorProduct = vendorProductService.getById(details.getVendorProductId());

            return TransactionDetail.builder()
                    .transaction(transaction)
                    .vendorProduct(vendorProduct)
                    .quantity(details.getQuantity())
                    .price(vendorProduct.getPrice())
                    .build();
        }).toList();

        transactionDetailService.createBulk(transactionDetails);
        transaction.setTransactionDetails(transactionDetails);

        //sava to warehouse
        warehouseService.create(transaction);

        //save to report
        reportService.createReport(transaction);

        //Transaction Response when success
        List<TransactionDetailResponse> transactionDetailResponses = transactionDetails.stream().map(
                detailResponse -> {

                    Product productById = productService.getById(detailResponse.getVendorProduct().getProduct().getId());

                    return TransactionDetailResponse.builder()
                            .productName(productById.getName())
                            .price(detailResponse.getPrice())
                            .quantity(detailResponse.getQuantity())
                            .build();
                }).toList();

        return TransactionResponse.builder()
                .id(transaction.getId())
                .vendorName(vendorService.getById(transaction.getVendor().getId()).getName())
                .transactionDate(transaction.getTransactionDate())
                .transactionDetailResponses(transactionDetailResponses)
                .build();
    }

    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest searchTransactionRequest) {

        if(searchTransactionRequest.getPage() <= 0) {
            searchTransactionRequest.setPage(1);
        }

        String validSortBy;
        if("vendorId".equalsIgnoreCase(searchTransactionRequest.getSortBy()) || "date".equalsIgnoreCase(searchTransactionRequest.getSortBy())){

            validSortBy = searchTransactionRequest.getSortBy();

        } else {

            validSortBy = "vendorId";

        }

        Sort sort = Sort.by(Sort.Direction.fromString(searchTransactionRequest.getDirection()), validSortBy);

        Pageable pageable = PageRequest.of((searchTransactionRequest.getPage() - 1), searchTransactionRequest.getSize(), sort);

        List<Transaction> transactions = transactionRepository.findAll();

        List<TransactionResponse> getAllTransaction = transactions.stream().map(
                transaction -> {
                    List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream().map(
                            detail -> {
                                Product product = productService.getById(detail.getVendorProduct().getProduct().getId());

                                return TransactionDetailResponse.builder()
                                        .productName(product.getName())
                                        .price(detail.getPrice())
                                        .quantity(detail.getQuantity())
                                        .build();
                            }).toList();
                    Vendor vendor = vendorService.getById(transaction.getVendor().getId());

                    return TransactionResponse.builder()
                            .id(transaction.getId())
                            .vendorName(vendor.getName())
                            .transactionDate(transaction.getTransactionDate())
                            .transactionDetailResponses(transactionDetailResponses)
                            .build();
                }).toList();



        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), getAllTransaction.size());

        List<TransactionResponse> pageContent = getAllTransaction.subList(start, end);

        return new PageImpl<>(pageContent, pageable, getAllTransaction.size());
    }
}

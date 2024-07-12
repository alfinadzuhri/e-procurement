package com.enigma.group5.e_procurement.service.Impl;


import com.enigma.group5.e_procurement.dto.request.SearchReportRequest;
import com.enigma.group5.e_procurement.entity.*;
import com.enigma.group5.e_procurement.entity.Report;
import com.enigma.group5.e_procurement.repository.ReportRepository;
import com.enigma.group5.e_procurement.service.ProductService;
import com.enigma.group5.e_procurement.service.ReportService;
import com.enigma.group5.e_procurement.service.VendorProductService;
import com.enigma.group5.e_procurement.specification.ReportSpecification;
import com.enigma.group5.e_procurement.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final VendorProductService vendorProductService;
    private final ProductService productService;

    private final ValidationUtil validationUtil;

    @Override
    public Page<Report> getAll(SearchReportRequest searchReportRequest) {
        if (searchReportRequest.getPage() <= 0){
            searchReportRequest.setPage(1);
        }
        String validSortBy;
        if ("transDate".equalsIgnoreCase(searchReportRequest.getSortBy())){
            validSortBy = searchReportRequest.getSortBy();
        }else {
            validSortBy = "transDate";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(searchReportRequest.getDirection()), validSortBy);

        Pageable pageable = PageRequest.of((searchReportRequest.getPage() -1), searchReportRequest.getSize(), sort); // rumus pagination

        Specification<Report> specification = ReportSpecification.getSpecification(searchReportRequest);

        return reportRepository.findAll(specification, pageable);
    }


    public List<Report> getReportsBetweenDate(Date startDate, Date endDate) {
//        if (searchReportRequest.getPage() <= 0){
//            searchReportRequest.setPage(1);
//        }
//        String validSortBy;
//        if ("startDate".equalsIgnoreCase(searchReportRequest.getSortBy())){
//            validSortBy = searchReportRequest.getSortBy();
//        }else {
//            validSortBy = "startDate";
//        }
//
//        Sort sort = Sort.by(Sort.Direction.fromString(searchReportRequest.getDirection()), validSortBy);
//
//        pageable = PageRequest.of((searchReportRequest.getPage() -1), searchReportRequest.getSize(), sort); // rumus pagination

        //Specification<Report> specification = ReportSpecification.getSpecification(searchReportRequest);

        //return reportRepository.findAll(specification, pageable);

        return reportRepository.findByTransDateBetween(startDate, endDate);
    }


    @Override
    public List<Report> createReport(Transaction transaction) {

        List<Report> reports = transaction.getTransactionDetails().stream().map(
                reportRequest -> {

                    VendorProduct vendorProduct = vendorProductService.getById(reportRequest.getVendorProduct().getId());
                    Product product = productService.getById(vendorProduct.getProduct().getId());

                    return Report.builder()
                            .transaction(transaction)
                            .id(vendorProduct.getId())
                            .transDate(transaction.getTransactionDate())
                            .productName(product.getName())
                            .vendorName(vendorProduct.getVendor().getName())
                            .category(vendorProduct.getProduct().getCategory())
                            .price(vendorProduct.getPrice())
                            .quantity(reportRequest.getQuantity())
                            .total(vendorProduct.getPrice() * reportRequest.getQuantity())
                            .build();
                }
        ).toList();

        return reportRepository.saveAllAndFlush(reports);
    }

    @Override
    public void exportToCSV(Page<Report> reports) {
        try (FileWriter writer = new FileWriter("report.csv")){
            writer.append("Transaction Date, Product Name, Price, Quantity, Total, TransactionId, ");
            for (Report report : reports) {
                        writer.append(report.getTransDate().toString()).append(",").
                        append(report.getProductName()).append(",").
                        append(report.getPrice().toString()).append(",").
                        append(report.getQuantity().toString()).append(",").
                        append(report.getTotal().toString()).append(",").
                        append(report.getTransaction().getId()).append(",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

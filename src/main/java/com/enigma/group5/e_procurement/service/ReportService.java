package com.enigma.group5.e_procurement.service;


import com.enigma.group5.e_procurement.dto.request.SearchReportRequest;
import com.enigma.group5.e_procurement.entity.Report;
import com.enigma.group5.e_procurement.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportService {

    List<Report> createReport(Transaction transaction);
    Page<Report> getAll(SearchReportRequest searchReportRequest);
    List<Report> getReportsBetweenDate(Date startDate, Date endDate);
    // For export to csv
    void exportToCSV(Page<Report> reports);
}

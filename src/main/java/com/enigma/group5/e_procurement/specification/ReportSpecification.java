package com.enigma.group5.e_procurement.specification;

import com.enigma.group5.e_procurement.dto.request.SearchReportRequest;
import com.enigma.group5.e_procurement.entity.Report;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {

    public static Specification<Report> getSpecification(SearchReportRequest searchReportRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchReportRequest.getTransDate() != null) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date parseDate = new Date();
//
//                try {
//                    parseDate = simpleDateFormat.parse(searchCustomerRequest.getBirthdate());
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate parseDate = LocalDate.parse(searchReportRequest.getTransDate(), dateTimeFormatter).atStartOfDay().toLocalDate();
                Predicate birthdatePred = criteriaBuilder.equal(root.get("transDate"), parseDate);

                predicates.add(birthdatePred);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
